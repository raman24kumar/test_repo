import java.nio.file.Paths

plugins {
    id("java")
    kotlin("jvm")
    id("com.diffplug.spotless")
}

//apply {
//    from(Paths.get("${project.rootDir}", "spotless.gradle"))
//}

sourceSets {
    main {
        java {
            srcDirs("src")
        }
        kotlin {
            srcDirs("src")
        }
        resources {
            srcDirs("resources")
        }
    }
}

repositories(RepositoryHandler::mavenCentral)

dependencies {
    val jacksonVersion: String by project
    val freemarkerVersion: String by project
    implementation(project(":parser"))
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-yaml
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion")
    implementation("org.freemarker:freemarker:$freemarkerVersion")
    implementation(project(mapOf("path" to ":validator")))
}
