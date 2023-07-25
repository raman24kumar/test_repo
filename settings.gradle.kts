rootProject.name = "Eagle-Gen"

pluginManagement {
    val spotlessVersion: String by settings
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm").version(kotlinVersion)
        application
        id("com.diffplug.spotless").version(spotlessVersion)
    }
}


include("generator")
include("parser")
include("main")
include("validator")
