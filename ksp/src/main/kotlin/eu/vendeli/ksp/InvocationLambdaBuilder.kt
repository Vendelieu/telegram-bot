package eu.vendeli.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.LONG
import com.squareup.kotlinpoet.SHORT
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.toTypeName

@Suppress("LongMethod", "CyclomaticComplexMethod")
internal fun FileBuilder.buildInvocationLambdaCodeBlock(
    function: KSFunctionDeclaration,
    injectableTypes: Map<TypeName, ClassName>,
) = buildCodeBlock {
    val isTopLvl = function.functionKind == FunctionKind.TOP_LEVEL
    val funQualifier = function.qualifiedName!!.getQualifier()
    val funName = if (!isTopLvl) {
        funQualifier.let { it + "::" + function.simpleName.getShortName() }
    } else {
        addImport(funQualifier, function.simpleName.getShortName())
        "::${function.simpleName.getShortName()}"
    }
    val isObject = (function.parent as? KSClassDeclaration)?.classKind == ClassKind.OBJECT

    beginControlFlow("suspendCall { classManager, update, user, bot, parameters ->").apply {
        var parametersEnumeration = ""
        if (!isTopLvl && !isObject && function.functionKind != FunctionKind.STATIC) {
            parametersEnumeration = "inst, "
            add(
                "val inst = classManager.getInstance(%L::class.java) as %L\n",
                funQualifier,
                funQualifier,
            )
        }
        function.parameters.forEachIndexed { index, parameter ->
            if (parameter.name == null) return@forEachIndexed
            val paramCall = (
                parameter.annotations.firstOrNull { i ->
                    i.shortName.asString() == "ParamMapping"
                }?.let { i ->
                    i.arguments.first { a -> a.name?.asString() == "name" }.value as? String
                } ?: parameter.name!!.getShortName()
            ).let {
                "parameters[\"$it\"]"
            }
            val typeName = parameter.type.toTypeName()
            val nullabilityMark = if (typeName.isNullable) "" else "!!"

            val value = when (typeName.copy(false)) {
                userClass -> "user$nullabilityMark"
                botClass -> "bot"
                STRING -> "$paramCall.toString()"
                INT, intPrimitiveType -> "$paramCall?.toIntOrNull()$nullabilityMark"
                LONG, longPrimitiveType -> "$paramCall?.toLongOrNull()$nullabilityMark"
                SHORT, shortPrimitiveType -> "$paramCall?.toShortOrNull()$nullabilityMark"
                FLOAT, floatPrimitiveType -> "$paramCall?.toFloatOrNull()$nullabilityMark"
                DOUBLE, doublePrimitiveType -> "$paramCall?.toDoubleOrNull()$nullabilityMark"

                updateClass -> "update"
                messageUpdClass -> "(update as? MessageUpdate)$nullabilityMark"
                callbackQueryUpdateClass -> "(update as? CallbackQueryUpdate)$nullabilityMark"
                editedMessageUpdateClass -> "(update as? EditedMessageUpdate)$nullabilityMark"
                channelPostUpdateClass -> "(update as? ChannelPostUpdate)$nullabilityMark"
                editedChannelPostUpdate -> "(update as? EditedChannelPostUpdate)$nullabilityMark"
                inlineQueryUpdateClass -> "(update as? InlineQueryUpdate)$nullabilityMark"
                chosenInlineResultUpdateClass -> "(update as? ChosenInlineResultUpdate)$nullabilityMark"
                shippingQueryUpdateClass -> "(update as? ShippingQueryUpdate)$nullabilityMark"
                preCheckoutQueryUpdateClass -> "(update as? PreCheckoutQueryUpdate)$nullabilityMark"
                pollUpdateClass -> "(update as? PollUpdate)$nullabilityMark"
                pollAnswerUpdateClass -> "(update as? PollAnswerUpdate)$nullabilityMark"
                myChatMemberUpdateClass -> "(update as? MyChatMemberUpdate)$nullabilityMark"
                chatMemberUpdateClass -> "(update as? ChatMemberUpdate)$nullabilityMark"
                chatJoinRequestUpdateClass -> "(update as? ChatJoinRequestUpdate)$nullabilityMark"

                in injectableTypes.keys -> {
                    val type = injectableTypes[typeName]!!
                    addImport(type.packageName, type.simpleName)
                    "(classManager.getInstance(${type.simpleName}::class.java) as ${type.simpleName}).get(update, bot)"
                }

                else -> "null"
            }
            add("val param%L = %L\n", index, value)
            parametersEnumeration += "param$index"
            if (index < function.parameters.lastIndex) parametersEnumeration += ", "
        }
        add("%L.invoke(\n\t%L\n)\n", funName, parametersEnumeration)
    }.endControlFlow().build()
}
