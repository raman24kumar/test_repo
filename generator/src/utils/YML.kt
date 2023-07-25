package com.suryadigital.eaglegen.generator.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.suryadigital.eaglegen.parser.types.Eagle
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

fun createYaml(mapper: ObjectMapper, path: Path, fileName: String, mapObject: Eagle) {
    if (!path.exists()) {
        path.createDirectories()
    }
    val yamlPath = Paths.get(path.toAbsolutePath().toString(), fileName)
    mapper.writeValue(yamlPath.toFile(), mapObject)
}
