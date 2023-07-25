package com.suryadigital.eaglegen.parser.types

import java.nio.file.Path

data class PackageConfiguration(
    val packageName: String,
    val database: DatabaseConfiguration,
    val repo: String,
)

data class DatabaseConfiguration(
    val dbHostName: String,
    val dbPortNumber: String,
    val dbName: String,
    val dbUser: String,
    val dbPassword: String,
)

data class EagleConfiguration(
    val inputFolder: Path,
    val outputFolder: Path,
    val packageConfiguration: PackageConfiguration,
)
