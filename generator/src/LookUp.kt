package com.suryadigital.eaglegen.generator

import com.suryadigital.eaglegen.generator.utils.createYaml
import com.suryadigital.eaglegen.generator.utils.generateServerImpl
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.types.RPC
import com.suryadigital.eaglegen.parser.types.RequestAndResponseType
import com.suryadigital.eaglegen.parser.types.Type
import com.suryadigital.eaglegen.parser.types.TypeFile
import com.suryadigital.eaglegen.parser.utils.getRPCType
import com.suryadigital.eaglegen.parser.utils.kebabCase
import com.suryadigital.eaglegen.parser.utils.titleCase
import com.suryadigital.eaglegen.validator.DataModelCrud
import com.suryadigital.eaglegen.validator.DataModelMethod
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

class LookUp(
    private val rpcParentDirectory: Path,
    private val ktParentDirectory: Path,
    private val modelledCrud: DataModelCrud,
    private val appConf: EagleConfiguration,
) {

    fun generate(yamlParser: YAMLParser) {
        for (field in modelledCrud.fields) {
            when (field.method) {
                is DataModelMethod.Lookup -> {
                    LookupModel(
                        metaInfo = listOf(
                            MetaModel(
                                folder = Paths.get("$ktParentDirectory", "queries"),
                                fileName = "Get${modelledCrud.crudName}${(field.method as DataModelMethod.Lookup).reference.tableName.titleCase()}.kt",
                                templatePath = "/lookup/queries/abstract-query.ftl",
                            ),
                            MetaModel(
                                folder = Paths.get("$ktParentDirectory", "queries"),
                                fileName = "Get${modelledCrud.crudName}${(field.method as DataModelMethod.Lookup).reference.tableName.titleCase()}Postgres.kt",
                                templatePath = "/lookup/queries/postgres-query.ftl",
                            ),
                            MetaModel(
                                ktParentDirectory,
                                fileName = "Get${modelledCrud.crudName}${field.name.titleCase()}LookupRPCServerImpl.kt",
                                templatePath = "/lookup/server-impl.ftl",
                            ),
                        ),
                        packageName = "${appConf.packageConfiguration.packageName}.${rpcParentDirectory.name}",
                        dependsOn = (field.method as DataModelMethod.Lookup).dependsOn?.let { it.column to it.field.type!!.eagleType},
                        rpcName = "Get${modelledCrud.crudName}${field.name.titleCase()}Lookup",
                        queryName = modelledCrud.crudName + (field.method as DataModelMethod.Lookup).reference.tableName,
                        keyName = (field.method as DataModelMethod.Lookup).reference.key.name,
                        valueName = (field.method as DataModelMethod.Lookup).reference.label.name,
                        tableName = (field.method as DataModelMethod.Lookup).reference.tableName,
                        columns = listOf(
                            Column(
                                columnName = "key",
                                columnValue = (field.method as DataModelMethod.Lookup).reference.key.name,
                                columnType = (field.method as DataModelMethod.Lookup).reference.key.type.name
                            ),
                            Column(
                                columnName = "value",
                                columnValue = (field.method as DataModelMethod.Lookup).reference.label.name,
                                columnType = (field.method as DataModelMethod.Lookup).reference.label.type.name
                            )
                        )
                    ).also {
                        generateRPC(yamlParser, it)
                        generateServerImpl(it)
                    }
                }

                null -> {
                }
            }
        }
    }

    private fun generateRPC(yamlParser: YAMLParser, lookupModel: LookupModel) {
        /**
         * For `lookup` we need to get the referring table and column
         * If the lookup is dependent on another field then that field has to be used as a request input.
         */
        val lookupResponseTypeName = lookupModel.rpcName + "Response"
        val yamlDataClass = RPC(
            name = lookupModel.rpcName,
            request = listOfNotNull(
                lookupModel.dependsOn?.let {
                    RequestAndResponseType(
                        name = it.first,
                        type = it.second.getRPCType().name,
                    )
                },
            ),
            response = listOf(
                RequestAndResponseType(
                    name = "result",
                    elementType = lookupResponseTypeName,
                ),
            ),
        )
        // Create a lookup response yaml file
        val lookupResponse = TypeFile(
            name = lookupResponseTypeName,
            attributes = lookupModel.columns.map {
                RequestAndResponseType(
                    name = it.columnName,
                    type = Type.valueOf(it.columnType).getRPCType().name,
                )
            },
        )
        // Create a Response type file for the lookup
        createYaml(
            mapper = yamlParser.mapper,
            path = rpcParentDirectory,
            fileName = lookupResponseTypeName.kebabCase() + "-type.yml",
            mapObject = lookupResponse,
        )
        // Create a lookup yaml file
        createYaml(
            mapper = yamlParser.mapper,
            path = rpcParentDirectory,
            fileName = "${lookupModel.rpcName.kebabCase()}-rpc.yml",
            mapObject = yamlDataClass,
        )
    }
}