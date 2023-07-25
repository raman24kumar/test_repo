package com.suryadigital.eaglegen.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.suryadigital.eaglegen.generator.utils.createRequestResponseOfAttribute
import com.suryadigital.eaglegen.generator.utils.createRequestResponseOfAttributeFromModel
import com.suryadigital.eaglegen.generator.utils.createYaml
import com.suryadigital.eaglegen.generator.utils.generateServerImpl
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.types.CRUD
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.types.ErrorType
import com.suryadigital.eaglegen.parser.types.RPC
import com.suryadigital.eaglegen.parser.types.RequestAndResponseType
import com.suryadigital.eaglegen.parser.utils.getRPCType
import com.suryadigital.eaglegen.parser.utils.kebabCase
import com.suryadigital.eaglegen.validator.DataModelCrud
import com.suryadigital.eaglegen.validator.DataModelField
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

class UpdateRequestGenerator(
    private val rpcParentDirectory: Path,
    private val ktParentDirectory: Path,
    private val modelledCrud: DataModelCrud,
    private val appConf: EagleConfiguration,
) {

    fun generate(yamlParser: YAMLParser) {
        generateYMLFile(modelledCrud, mapper = yamlParser.mapper)
        val requestFields = modelledCrud.fields.filter(DataModelField::isFormField).map(::createRequestResponseOfAttributeFromModel)
        CreateAndUpdateRequestDataModel(
            metaInfo = listOf(
                MetaModel(
                    folder = Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Update${modelledCrud.crudName}Request.kt",
                    templatePath = "/update-request/queries/update-abstract-class.ftl",
                ),
                MetaModel(
                    folder = Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Update${modelledCrud.crudName}RequestPostgres.kt",
                    templatePath = "/update-request/queries/update-query.ftl",
                ),
                MetaModel(
                    Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Find${modelledCrud.crudName}Request.kt",
                    templatePath = "/update-request/queries/get-request-abstract-class.ftl",
                ),
                MetaModel(
                    Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Find${modelledCrud.crudName}RequestPostgres.kt",
                    templatePath = "/update-request/queries/get-request-postgres-query.ftl",
                ),
                MetaModel(
                    ktParentDirectory,
                    fileName = "Update${modelledCrud.crudName}RequestRPCServerImpl.kt",
                    templatePath = "/update-request/server-impl.ftl",
                ),
            ),
            packageName = "${appConf.packageConfiguration.packageName}.${rpcParentDirectory.name}",
            rpcName = "Update${modelledCrud.crudName}Request",
            tableName = modelledCrud.tableToSave.tableName,
            queryName = "${modelledCrud.crudName}Request",
            columns = requestFields.map {
                Column(
                    columnName = it.name,
                    columnType = it.type,
                    optional = it.optional ?: false,
                )
            },
            primaryKeyColumn = Column(
                columnName = modelledCrud.fields.find { it.name == modelledCrud.tableToSave.primaryKeyColumn }?.name!!,
                columnType = modelledCrud.fields.find { it.name == modelledCrud.tableToSave.primaryKeyColumn }?.type?.rpcType?.name
                    ?: throw IllegalArgumentException("The primary key field type was not found"),
            ),
        ).also(::generateServerImpl)
    }

    private fun generateYMLFile(modelledCrud: DataModelCrud, mapper: ObjectMapper) {
        createYaml(
            mapper = mapper,
            fileName = "Update${modelledCrud.crudName}Request".kebabCase() + "-rpc.yml",
            path = rpcParentDirectory,
            mapObject = RPC(
                name = "Update${modelledCrud.crudName}Request",
                request = listOf(
                    RequestAndResponseType(
                        name = modelledCrud.tableToSave.primaryKeyColumn,
                        type = modelledCrud.fields.find { it.name == modelledCrud.tableToSave.primaryKeyColumn }?.type?.name!!,
                    ),
                ).plus(
                    modelledCrud.fields.filter(DataModelField::isFormField).map(::createRequestResponseOfAttributeFromModel),
                ),
                response = null,
                errors = listOf(ErrorType("INVALID_REQUEST_ID")),
            ),
        )
    }
}