import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Jdbc
import org.jooq.meta.jaxb.Target
import org.jooq.meta.jaxb.Configuration

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath 'org.jooq:jooq-codegen:3.16.12'
        classpath 'org.postgresql:postgresql:42.5.1'
    }
}

apply plugin: 'kotlin'

sourceSets {
    main.kotlin.srcDirs = main.java.srcDirs = ['src']
}

dependencies {
    implementation 'org.jooq:jooq:3.16.12'
    runtimeOnly "org.postgresql:postgresql:42.5.1"
}

tasks.register('generateJooqCode') {
    doLast {
        def dbHostName = System.getenv("DB_HOST") ?: "${dbHostName}"
        def dbPortNumber = System.getenv("DB_PORT") ?: "${dbPortNumber}"
        def dbName = System.getenv("DB_NAME") ?: "${dbName}"
        def dbUser = System.getenv("DB_USER_NAME") ?: "${dbUser}"
        def dbPassword = System.getenv("DB_PASSWORD") ?: "${dbPassword}"
        GenerationTool.generate(new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver('org.postgresql.Driver')
                        .withUrl("jdbc:postgresql://$dbHostName:$dbPortNumber/$dbName")
                        .withUser(dbUser)
                        .withPassword(dbPassword))
                .withGenerator(new Generator()
                        .withName('org.jooq.codegen.KotlinGenerator')
                        .withDatabase(new Database()
                                .withInputSchema('public'))
                        .withGenerate(new Generate().withRoutines(false))
                        .withTarget(new Target()
                                .withPackageName('${packageName}.jooq')
                                .withDirectory("${r"${projectDir}/src"}")
                                .withClean(true)
                        )))
    }
}

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
            artifactId 'jooq'
            from(components.java)
            artifact sourcesJar
        }
    }
}