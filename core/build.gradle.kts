plugins {
    id("signing")
    id("maven-publish")
    alias(libs.plugins.kotlin.jvm)
}

publishing {
    repositories {
        if (version.toString().contains("-SNAPSHOT")) {
            maven {
                name = "atlasislesSnapshots"
                url = uri("https://mvn.atlasisles.com/snapshots")
                credentials(PasswordCredentials::class)
                authentication { create<BasicAuthentication>("basic") }
            }
        } else {
            maven {
                name = "atlasislesReleases"
                url = uri("https://mvn.atlasisles.com/releases")
                credentials(PasswordCredentials::class)
                authentication { create<BasicAuthentication>("basic") }
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.atlasisles"
            artifactId = "adventure-kt"
            version = version.toString()

            from(components["kotlin"])

            pom {
                name = "adventure-kt"
                description = "A custom Kotlin syntax for the Adventure UI library"
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
    }
}