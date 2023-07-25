plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "com.suryadigital"
version = "0.0.1"

repositories(RepositoryHandler::mavenCentral)

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}
