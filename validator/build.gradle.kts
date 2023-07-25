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
    implementation(project(":parser"))

}
