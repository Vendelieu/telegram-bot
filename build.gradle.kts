allprojects {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    group = "eu.vendeli"
    version = providers.gradleProperty("libVersion").getOrElse("dev")
}

plugins {
    alias(libs.plugins.ktlinter) apply false
    alias(libs.plugins.deteKT) apply false
}

tasks.create("prepareRelease") {
    dependsOn("telegram-bot:kdocUpdate")
    dependsOn("telegram-bot:formatKotlin")
    dependsOn("ktgram-gradle-plugin:formatKotlin")
    dependsOn("helper:formatKotlin")

    dependsOn("ksp:detekt")
    dependsOn("telegram-bot:detekt")

    dependsOn("telegram-bot:clean")
    dependsOn("telegram-bot:kspCommonMainKotlinMetadata")
    dependsOn("telegram-bot:apiCheck")
}
