import java.nio.file.Paths

buildscript {
    repositories {
        mavenCentral()
    }
}

apply plugin: 'kotlin'

sourceSets {
    main.kotlin.srcDirs = main.java.srcDirs = ['src']
    main.resources.srcDirs = ['resources']
}

repositories {
    maven { url 'https://kotlin.bintray.com/ktor' }
}

dependencies {
    api project(":${name}-interface")
    api project(":${name}-service-boilerplate")
    api project(":jooq")
    implementation "com.suryadigital.leo:types:$leo_version"
    implementation "com.suryadigital.leo:basedb:$leo_version"
    implementation "com.suryadigital.leo:inline-logger:$leo_version"

    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"

    implementation "org.slf4j:slf4j-api:$slf4j_version"
    implementation "io.insert-koin:koin-ktor:$koin_version"
    implementation "io.insert-koin:koin-logger-slf4j:$koin_version"
}

compileKotlin {
    kotlinOptions.allWarningsAsErrors = true
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17
    kotlinOptions {
        freeCompilerArgs = ["-Xinline-classes"]
    }
}

apply from: Paths.get("${r"${project.rootDir}"}", "kotlin.gradle")

tasks.register('sourcesJar', Jar) {
    archiveClassifier = 'sources'
    from sourceSets.main.allSource
}
artifacts {
    archives sourcesJar, jar
}

publishing {
    publications {
        gpr(MavenPublication) {
            artifactId '${name}-service-impl'
            from(components.java)
            artifact sourcesJar
        }
    }
}

compileKotlin.dependsOn(":jooq:generateJooqCode")
