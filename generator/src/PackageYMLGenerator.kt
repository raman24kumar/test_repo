package com.suryadigital.eaglegen.generator

import com.suryadigital.eaglegen.generator.utils.createYaml
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.types.PackageYml
import java.nio.file.Path
import kotlin.io.path.name

class PackageYMLGenerator(private val parentDirectory: Path, private val appConf: EagleConfiguration) {
    private val yamlParser = YAMLParser()

    fun execute() {
        createPackageYmlFile(appConf = appConf)
    }

    private fun createPackageYmlFile(appConf: EagleConfiguration) {
        createYaml(
            mapper = yamlParser.mapper,
            path = parentDirectory,
            fileName = "package.yml",
            mapObject = PackageYml(
                packageName = appConf.packageConfiguration.packageName + "." + parentDirectory.name,
                name = parentDirectory.name,
            ),
        )
    }
}
