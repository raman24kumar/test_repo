package com.suryadigital.eaglegen.generator

import com.suryadigital.eaglegen.generator.models.KoinModuleDefinitionModel
import com.suryadigital.eaglegen.generator.utils.generateServerImpl
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.utils.titleCase
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

class KoinModuleGenerator(private val appConf: EagleConfiguration, private val ktParentDirectory: Path) {
    private val generatedParentDir =
        Paths.get(appConf.outputFolder.toString(), "artifacts", ktParentDirectory.name, "src")

    fun generateFile() {
        val dbFiles =
            Paths.get("$generatedParentDir", "queries").toFile()
                .listFiles()
        val databaseModule = dbFiles!!
            .asSequence()
            .filterNot { it.nameWithoutExtension.endsWith("Postgres") }
            .map {
                it.nameWithoutExtension to it.nameWithoutExtension + "Postgres"
            }
            .toList()
        val files = generatedParentDir.toFile().listFiles()
        val routesModule = files!!
            .asSequence()
            .filterNot { it.nameWithoutExtension.endsWith("queries") }
            .map {
                it.nameWithoutExtension.replace("ServerImpl", "") to it.nameWithoutExtension
            }
            .toList()
        generateServerImpl(
            dataModel = KoinModuleDefinitionModel(
                metaInfo = listOf(
                    MetaModel(
                        folder = generatedParentDir,
                        fileName = "${ktParentDirectory.name.titleCase()}Module.kt",
                        templatePath = "/boilerplate/koin-module.ftl",
                    ),
                ),
                packageName = appConf.packageConfiguration.packageName + "." + ktParentDirectory.name,
                databaseModule = databaseModule,
                routesModule = routesModule,
                folderName = ktParentDirectory.name,
            ),
        )
    }
}
