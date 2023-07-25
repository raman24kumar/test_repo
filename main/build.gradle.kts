import java.nio.file.Paths

plugins {
    id("java")
    kotlin("jvm")
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
