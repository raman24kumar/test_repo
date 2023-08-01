import java.nio.file.Paths

plugins {
    kotlin("jvm") version "1.8.21"
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
    id("com.diffplug.spotless")
}

apply {
    from(Paths.get("${project.rootDir}", "spotless.gradle"))
}

group = "com.suryadigital"
version = "1.0-SNAPSHOT"

repositories(RepositoryHandler::mavenCentral)
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

application {
    mainClass.set("com.suryadigital.eaglegen.main.MainKt")
}

tasks {
    shadowJar {
        archiveFileName.set("EagleGen-${project.name}.jar")
    }
}

dependencies {
    val jacksonVersion: String by project
    val apacheCommons: String by project
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation(project(":parser"))
    implementation(project(":validator"))
    implementation(project(":generator"))
    implementation("commons-io:commons-io:$apacheCommons")
}
