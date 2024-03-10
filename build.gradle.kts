import org.gradle.api.tasks.bundling.Jar

plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.dokka") version "1.9.20"
    id("me.qoomon.git-versioning") version "6.4.2"
    id("maven-publish")
    id("signing")
    id("distribution")
}

val SIGNING_KEY: String? by project
val SIGNING_PASSWORD: String? by project

group = "io.github.memoryhole"
version = "0.0.0-SNAPSHOT"
gitVersioning.apply {
    refs {
        tag("v(?<version>.*)") {
            version = "\${ref.version}"
        }
    }

    // optional fallback configuration in case of no matching ref configuration
    rev {
        version = "\${commit}"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("PASSED", "SKIPPED", "FAILED", "STANDARD_OUT", "STANDARD_ERROR")
    }
}

kotlin {
    jvmToolchain(20)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier = "sources"
    from(sourceSets.main.get().allSource)
}

val javadocJar = tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.publish {
    dependsOn("dokkaJavadocJar")
}

tasks.distZip {
    dependsOn("publish")
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}


publishing {
    repositories {
        maven {
            // change to point to your repo, e.g. http://my.org/repo
            url = uri(layout.buildDirectory.dir("repo").get().asFile)
        }
    }
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())

            pom {
                description = "library to convert between the Amateur Radio Maidenhead Locator System and latitude/longitude coordinates"
                url = "https://github.com/memoryhole/gridsquare"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://raw.githubusercontent.com/memoryhole/gridsquare/main/LICENSE"
                    }
                }

                developers {
                    developer {
                        name = "Alexander Wolfe"
                        id = "memoryhole"
                        email = "dev@five381.com"
                    }
                }

                scm {
                    connection = "git@github.com:memoryhole/gridsquare.git"
                    developerConnection = "git@github.com:memoryhole/gridsquare.git"
                    url = "https://github.com/memoryhole/gridsquare"
                }
            }
        }
    }
}

distributions {
    main {
        contents {
            from(layout.buildDirectory.dir("repo").get().asFile) {
                exclude("**/maven-metadata.*")
            }
            into("/")
        }
    }
}

signing {
    if ((SIGNING_KEY?:"").isNotEmpty()) {
        useInMemoryPgpKeys(SIGNING_KEY, SIGNING_PASSWORD)
    } else {
        useGpgCmd()
    }
    sign(publishing.publications["mavenJava"])
}