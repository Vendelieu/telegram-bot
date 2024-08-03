package eu.vendeli.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import eu.vendeli.ksp.dto.CommonAnnotationData
import eu.vendeli.ksp.utils.FileBuilder
import eu.vendeli.ksp.utils.addMap
import eu.vendeli.ksp.utils.commonMatcherClass
import eu.vendeli.ksp.utils.invocableType
import eu.vendeli.ksp.utils.parseAsCommandHandler
import eu.vendeli.ksp.utils.parseAsInputHandler
import eu.vendeli.ksp.utils.parseAsUpdateHandler
import eu.vendeli.ksp.utils.toRateLimits
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.annotations.CommandHandler.CallbackQuery
import eu.vendeli.tgbot.annotations.InputHandler
import eu.vendeli.tgbot.annotations.UpdateHandler
import eu.vendeli.tgbot.types.internal.UpdateType

internal fun FileBuilder.collectCommandActivities(
    symbols: Sequence<KSFunctionDeclaration>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
    idxPostfix: String,
    pkg: String? = null
) {
    logger.info("Collecting commands.")
    addMap(
        "__TG_COMMANDS$idxPostfix",
        MAP.parameterizedBy(
            Pair::class.asTypeName().parameterizedBy(STRING, UpdateType::class.asTypeName()),
            invocableType,
        ),
        symbols,
    ) { function ->
        var isCallbackQAnnotation = false
        val annotationData = function.annotations
            .first {
                val shortName = it.shortName.asString()
                when (shortName) {
                    CallbackQuery::class.simpleName -> {
                        isCallbackQAnnotation = true
                        true
                    }

                    CommandHandler::class.simpleName -> true
                    else -> false
                }
            }.arguments
            .parseAsCommandHandler(isCallbackQAnnotation)

        annotationData.value.forEach {
            annotationData.scope.forEach { updT ->
                logger.info("Command: $it UpdateType: ${updT.name} --> ${function.qualifiedName?.asString()}")

                addStatement(
                    "(\"$it\" to %L) to (%L to InvocationMeta(\"%L\", \"%L\", %L, %L::class)),",
                    updT,
                    buildInvocationLambdaCodeBlock(function, injectableTypes, pkg),
                    function.qualifiedName!!.getQualifier(),
                    function.simpleName.asString(),
                    annotationData.rateLimits.toRateLimits(),
                    annotationData.guardClass,
                )
            }
        }
    }
}

internal fun FileBuilder.collectInputActivities(
    symbols: Sequence<KSFunctionDeclaration>,
    chainSymbols: Sequence<KSClassDeclaration>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
    idxPostfix: String,
    pkg: String? = null
) {
    logger.info("Collecting inputs.")
    val tailBlock = collectInputChains(chainSymbols, logger)

    addMap(
        "__TG_INPUTS$idxPostfix",
        MAP.parameterizedBy(STRING, invocableType),
        symbols,
        tailBlock,
    ) { function ->
        val annotationData = function.annotations
            .first {
                it.shortName.asString() == InputHandler::class.simpleName!!
            }.arguments
            .parseAsInputHandler()
        annotationData.first.forEach {
            logger.info("Input: $it --> ${function.qualifiedName?.asString()}")
            addStatement(
                "\"$it\" to (%L to InvocationMeta(\"%L\", \"%L\", %L, %L::class)),",
                buildInvocationLambdaCodeBlock(function, injectableTypes, pkg),
                function.qualifiedName!!.getQualifier(),
                function.simpleName.asString(),
                annotationData.second.toRateLimits(),
                annotationData.third,
            )
        }
    }
}

internal fun FileBuilder.collectUpdateTypeActivities(
    symbols: Sequence<KSFunctionDeclaration>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
    idxPostfix: String,
) {
    logger.info("Collecting `UpdateType` handlers.")
    addMap(
        "__TG_UPDATE_TYPES$idxPostfix",
        MAP.parameterizedBy(UpdateType::class.asTypeName(), TypeVariableName("InvocationLambda")),
        symbols,
    ) { function ->
        val annotationData = function.annotations
            .first {
                it.shortName.asString() == UpdateHandler::class.simpleName!!
            }.arguments
            .parseAsUpdateHandler()

        annotationData.forEach {
            logger.info("UpdateType: ${it.name} --> ${function.qualifiedName?.asString()}")
            addStatement(
                "%L to %L,",
                it,
                buildInvocationLambdaCodeBlock(function, injectableTypes),
            )
        }
    }
}

internal fun FileBuilder.collectCommonActivities(
    data: List<CommonAnnotationData>,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
    idxPostfix: String,
    pkg: String? = null
) {
    logger.info("Collecting common handlers.")
    addProperty(
        PropertySpec
            .builder(
                "__TG_COMMONS$idxPostfix",
                MAP.parameterizedBy(commonMatcherClass, invocableType),
                KModifier.PRIVATE,
            ).apply {
                initializer(
                    CodeBlock
                        .builder()
                        .apply {
                            add("mapOf(\n")
                            data.forEach {
                                addStatement(
                                    "%L to (%L to InvocationMeta(\"%L\", \"%L\", %L)),",
                                    it.value.toCommonMatcher(it.filter, it.scope),
                                    buildInvocationLambdaCodeBlock(it.funDeclaration, injectableTypes, pkg),
                                    it.funQualifier,
                                    it.funSimpleName,
                                    it.rateLimits.let { l ->
                                        if (l.rate == 0L &&
                                            l.period == 0L
                                        ) "zeroRateLimits" else l
                                    },
                                )
                            }
                            add(")\n")
                        }.build(),
                )
            }.build(),
    )
}

internal fun FileBuilder.collectUnprocessed(
    unprocessedHandlerSymbols: KSFunctionDeclaration?,
    injectableTypes: Map<TypeName, ClassName>,
    logger: KSPLogger,
    idxPostfix: String,
) {
    addProperty(
        PropertySpec
            .builder(
                "__TG_UNPROCESSED$idxPostfix",
                TypeVariableName("InvocationLambda").copy(true),
                KModifier.PRIVATE,
            ).apply {
                initializer(
                    buildCodeBlock {
                        add(
                            "%L",
                            unprocessedHandlerSymbols?.let {
                                logger.info("Unprocessed handler --> ${it.qualifiedName?.asString()}")
                                buildInvocationLambdaCodeBlock(it, injectableTypes)
                            },
                        )
                    },
                )
            }.build(),
    )
}
