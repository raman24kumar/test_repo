package com.suryadigital.eaglegen.generator

import com.suryadigital.eaglegen.generator.models.JooqModel
import com.suryadigital.eaglegen.generator.utils.generateServerImpl
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import java.nio.file.Paths

class JooqGradleGenerator(appConf: EagleConfiguration) {
    private val yamlParser = YAMLParser()

    init {
        generateServerImpl(
            dataModel = JooqModel(
                metaInfo = listOf(
                    MetaModel(
                        folder = Paths.get(appConf.outputFolder.toString(), "artifacts", "jooq"),
                        fileName = "build.gradle",
                        templatePath = "/boilerplate/jooq-gradle.ftl",
                    ),
                ),
                dbHostName = appConf.packageConfiguration.database.dbHostName,
                dbName = appConf.packageConfiguration.database.dbName,
                packageName = appConf.packageConfiguration.packageName,
                dbPassword = appConf.packageConfiguration.database.dbPassword,
                dbPortNumber = appConf.packageConfiguration.database.dbPortNumber,
                dbUser = appConf.packageConfiguration.database.dbUser,
            ),
        )
    }
}
