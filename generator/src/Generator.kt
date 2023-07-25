package com.suryadigital.eaglegen.generator

import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.parseYMLFile
import com.suryadigital.eaglegen.parser.types.CRUD
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.validator.DataModelCrud
import com.suryadigital.eaglegen.validator.MetadataValidator

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

class Generator(
    private val file: Path,
    private val yamlParser: YAMLParser,
    private val appConfiguration: EagleConfiguration,
) {
    fun execute() {
        val rpcParentDirectory =
            Paths.get(appConfiguration.outputFolder.name, "generated-rpcs", file.parent.name).toAbsolutePath()
        val ktParentDirectory =
            Paths.get(appConfiguration.outputFolder.name, "artifacts", file.parent.name, "src").toAbsolutePath()
        val crud = parseYMLFile(file = file, mapper = yamlParser.mapper, classType = CRUD::class.java)
        val modelledCrud = MetadataValidator(file, crud).generateDataModel()
        PackageYMLGenerator(parentDirectory = rpcParentDirectory, appConf = appConfiguration).execute()
        LookUp(
            rpcParentDirectory = rpcParentDirectory,
            modelledCrud = modelledCrud,
            ktParentDirectory = ktParentDirectory,
            appConf = appConfiguration,
        ).generate(yamlParser)
        CreateRequestGenerator(
            rpcParentDirectory = rpcParentDirectory,
            modelledCrud = modelledCrud,
            ktParentDirectory = ktParentDirectory,
            appConf = appConfiguration,
        ).generate(yamlParser)
        PaginationGenerator(
            rpcParentDirectory = rpcParentDirectory,
            modelledCrud = modelledCrud,
            ktParentDirectory = ktParentDirectory,
            appConf = appConfiguration,
        ).generate(yamlParser)
        RequestDetailsGenerator(
            rpcParentDirectory = rpcParentDirectory,
            modelledCrud = modelledCrud,
            ktParentDirectory = ktParentDirectory,
            appConf = appConfiguration,
        ).generate(yamlParser)
        UpdateRequestGenerator(
            rpcParentDirectory = rpcParentDirectory,
            modelledCrud = modelledCrud,
            ktParentDirectory = ktParentDirectory,
            appConf = appConfiguration,
        ).generate(yamlParser)
    }
}
