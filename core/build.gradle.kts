import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.vanniktech.maven.publish") version "0.31.0"
    alias(libs.plugins.kotlin.jvm)
}

mavenPublishing {
    if (project.name != "core") return@mavenPublishing
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("com.atlasisles", "adventure-kt", version.toString())

    pom {
        name = "adventure-kt"
        description = "Kotlin extensions for the adventure UI library"
        inceptionYear = "2025"
        url = "https://github.com/atlasisles/adventure-kt"
        licenses {
            license {
                name = "The MIT Licence"
                url = "https://opensource.org/licenses/MIT"
                distribution = "https://github.com/atlasisles/adventure-kt/blob/master/LICENSE"
            }
        }
        developers {
            developer {
                id = "tocularity"
                url = "https://github.com/tocularity"
            }
        }
        scm {
            url = "https://github.com/atlasisles/adventure-kt"
            connection = "scm:git:git://github.com/atlasisles/adventure-kt.git"
            developerConnection = "scm:git:ssh://git@github.com/atlasisles/adventure-kt.git"
        }
    }
}