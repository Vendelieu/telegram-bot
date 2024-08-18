import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish")
}

val libraryData = extensions.create("libraryData", PublishingExtension::class)
val releaseMode = providers.gradleProperty("signing.keyId").isPresent
val isMultiplatform = !project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")

if (isMultiplatform) apply(plugin = "org.jetbrains.kotlin.multiplatform")

mavenPublishing {
    coordinates("eu.vendeli", project.name, project.version.toString())
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    if (releaseMode) signAllPublications()

    val javaDoc = if (project.name == "telegram-bot" && releaseMode) JavadocJar.Dokka("dokkaHtml")
    else JavadocJar.Empty()

    val platformArtifact = if (isMultiplatform) KotlinMultiplatform(javaDoc, true)
    else KotlinJvm(JavadocJar.None(), true)

    configure(platformArtifact)

    pom {
        name = libraryData.name
        description = libraryData.description
        inceptionYear = "2022"
        url = "https://github.com/vendelieu/telegram-bot"

        licenses {
            license {
                name = "Apache 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }
        developers {
            developer {
                id = "Vendelieu"
                name = "Vendelieu"
                email = "vendelieu@gmail.com"
                url = "https://vendeli.eu"
            }
        }
        scm {
            connection = "scm:git:github.com/vendelieu/telegram-bot.git"
            developerConnection = "scm:git:ssh://github.com/vendelieu/telegram-bot.git"
            url = "https://github.com/vendelieu/telegram-bot.git"
        }
        issueManagement {
            system = "Github"
            url = "https://github.com/vendelieu/telegram-bot/issues"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GHPackages"
            url = uri("https://maven.pkg.github.com/vendelieu/telegram-bot")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
