import java.nio.file.Paths

plugins {
    kotlin("jvm")
    application
    id("com.diffplug.spotless")
}

apply {
    from(Paths.get("${project.rootDir}", "spotless.gradle"))
}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        kotlin {
            srcDirs("src")
        }
    }
}

repositories(RepositoryHandler::mavenCentral)

dependencies {
    val jacksonVersion: String by project
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
}
