package com.suryadigital.eaglegen.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.types.PackageConfiguration
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class YAMLParser {
    val mapper: ObjectMapper

    init {
        val yamlFactory = YAMLFactory()
        yamlFactory.let {
            it.configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false)
            it.configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, true)
        }
        mapper = ObjectMapper(yamlFactory)
        mapper.registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, true)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build(),
        )
    }
}

fun getConfiguration(mapper: ObjectMapper, args: Array<String>): EagleConfiguration {
    return EagleConfiguration(
        inputFolder = Paths.get(args[0]),
        outputFolder = Paths.get(args[1]),
        packageConfiguration = parseYMLFile(mapper = mapper, file = Paths.get(args[0], "configuration.yml"), classType = PackageConfiguration::class.java),
    )
}

fun <T> parseYMLFile(mapper: ObjectMapper, file: Path, classType: Class<T>): T {
    return try {
        Files.newBufferedReader(file).use {
            mapper.readValue(it, classType)
        }
    } catch (exception: MismatchedInputException) {
        throw exception
    }
}
