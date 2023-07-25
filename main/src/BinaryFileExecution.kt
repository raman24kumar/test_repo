package com.suryadigital.eaglegen.main

import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.io.path.deleteIfExists

class BinaryFileExecution(private val appConf: EagleConfiguration) {

    fun executeRPCGen() {
        // 1) generate artifactory.properties file
//        copyArtifactoryProperties()
        // 2) Generate the interface code
        generateInterfaceCode()
        // 3) Generate Boilerplate code
        generateBoilerplateCode()
        // 4) Generate Client Code
        generateClientCode()
        // 5) Append the subdirectories in the settings.gradle
        appendTheGeneratedDirectoriesInSettings()
    }

    private fun generateClientCode() {
        "./bin/rpc-gen-linux kotlin-jvm-client -f -d ./${appConf.outputFolder}/generated-rpcs -o ./${appConf.outputFolder}/artifacts".runCommand(
            File(System.getProperty("user.dir")),
        )
    }

    private fun generateBoilerplateCode() {
        "./bin/rpc-gen-linux kotlin-service-boilerplate -f -d ./${appConf.outputFolder}/generated-rpcs -o ./${appConf.outputFolder}/artifacts".runCommand(
            File(System.getProperty("user.dir")),
        )
    }

    private fun appendTheGeneratedDirectoriesInSettings() {
        val stringBuilder = StringBuilder()
        stringBuilder.append("rootProject.name='${appConf.packageConfiguration.packageName.split(".").last()}'\n")
        val files = File("${appConf.outputFolder}/artifacts").listFiles()
        val excludedFolders = listOf(
            "gradle",
        )
        files!!
            .asSequence()
            .filter { it.isDirectory && !excludedFolders.contains(it.name) }
            .forEach {
                stringBuilder.append(
                    "include \":${it.name}\"\n",
                )
            }
        val settingsPath = Paths.get(appConf.outputFolder.toString(), "artifacts", "settings.gradle")
        settingsPath.deleteIfExists()
        Files.write(
            settingsPath,
            "$stringBuilder".toByteArray(),
            StandardOpenOption.CREATE,
        )
    }

    private fun generateInterfaceCode() {
        "./bin/rpc-gen-linux kotlin-interface -f -d ./${appConf.outputFolder}/generated-rpcs -o ./${appConf.outputFolder}/artifacts -g ${appConf.packageConfiguration.packageName} -r ${appConf.packageConfiguration.repo}".runCommand(
            File(System.getProperty("user.dir")),
        )
    }

    private fun copyArtifactoryProperties() {
        val artifactoryPath = Paths.get("artifactory.properties")
        if (!Files.exists(artifactoryPath)) {
            throw IllegalStateException("artifactory.properties are not present in the project")
        }
        copyFilesToArtifacts(artifactoryPath, artifactoryPath)
    }

    private fun copyFilesToArtifacts(sourcePath: Path, fileName: Path) {
        if (Files.isDirectory(sourcePath)) {
            FileUtils.copyDirectory(sourcePath.toFile(), fileName.toFile())
        } else {
            Files.copy(sourcePath, Paths.get(appConf.outputFolder.toString(), "artifacts", "$fileName"))
        }
    }
}
