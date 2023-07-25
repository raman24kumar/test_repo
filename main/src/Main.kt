package com.suryadigital.eaglegen.main

import com.suryadigital.eaglegen.generator.Generator
import com.suryadigital.eaglegen.generator.JooqGradleGenerator
import com.suryadigital.eaglegen.generator.KoinModuleGenerator
import com.suryadigital.eaglegen.generator.MetaModel
import com.suryadigital.eaglegen.generator.models.GradleModel
import com.suryadigital.eaglegen.generator.utils.generateGradleImpl
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.getConfiguration
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.validator.MetadataValidator
import java.nio.file.Path
import java.nio.file.Paths
import javax.xml.validation.Validator
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

@OptIn(ExperimentalPathApi::class)
fun main(args: Array<String>) {
    val yamlParser = YAMLParser()
    val appConf = getConfiguration(mapper = yamlParser.mapper, args)
    appConf.outputFolder.deleteRecursively()
    parseYmlFilesAndGenerateCode(folder = appConf.inputFolder, yamlParser = yamlParser, appConf = appConf)
    JooqGradleGenerator(appConf)
    BinaryFileExecution(appConf).executeRPCGen()
}

fun parseYmlFilesAndGenerateCode(
    folder: Path,
    yamlParser: YAMLParser,
    appConf: EagleConfiguration,
) {
    val files = folder.toFile().listFiles()
    for (file in files!!) {
        if (file.isDirectory) {
            parseYmlFilesAndGenerateCode(folder = file.toPath(), yamlParser = yamlParser, appConf = appConf)
            generateGradleImpl(
                dataModel = GradleModel(
                    metaModel = MetaModel(
                        folder = Paths.get(appConf.outputFolder.toString(), "artifacts", file.name),
                        fileName = "build.gradle",
                        templatePath = "/boilerplate/gradle.ftl",
                    ),
                    name = file.name,
                ),
            )
//            KoinModuleGenerator(appConf = appConf, ktParentDirectory = file.toPath()).generateFile()
        }
        if (file.name.contains("crud.yml")) {
            Generator(file = file.toPath(), yamlParser = yamlParser, appConfiguration = appConf).execute()
        }
    }
}
