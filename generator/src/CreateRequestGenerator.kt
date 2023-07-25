package com.suryadigital.eaglegen.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.suryadigital.eaglegen.generator.utils.createRequestResponseOfAttribute
import com.suryadigital.eaglegen.generator.utils.createRequestResponseOfAttributeFromModel
import com.suryadigital.eaglegen.generator.utils.createYaml
import com.suryadigital.eaglegen.generator.utils.generateServerImpl
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.types.CRUD
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.types.RPC
import com.suryadigital.eaglegen.parser.types.RPCTypes
import com.suryadigital.eaglegen.parser.types.RequestAndResponseType
import com.suryadigital.eaglegen.parser.utils.getRPCType
import com.suryadigital.eaglegen.parser.utils.kebabCase
import com.suryadigital.eaglegen.validator.DataModelCrud
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

class CreateRequestGenerator(
    private val rpcParentDirectory: Path,
    private val ktParentDirectory: Path,
    private val modelledCrud: DataModelCrud,
    private val appConf: EagleConfiguration,
) {
    fun generate(yamlParser: YAMLParser) {
        val requestFields = modelledCrud.fields.map(::createRequestResponseOfAttributeFromModel)
        CreateRequestDataModel(
            metaInfo = listOf(
                MetaModel(
                    folder = Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Insert${modelledCrud.crudName}Request.kt",
                    templatePath = "/create-request/queries/abstract-query.ftl",
                ),
                MetaModel(
                    folder = Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Insert${modelledCrud.crudName}RequestPostgres.kt",
                    templatePath = "/create-request/queries/postgres-query.ftl",
                ),
                MetaModel(
                    ktParentDirectory,
                    fileName = "Create${modelledCrud.crudName}RequestRPCServerImpl.kt",
                    templatePath = "/create-request/server-impl.ftl",
                ),
            ),
            packageName = "${appConf.packageConfiguration.packageName}.${rpcParentDirectory.name}",
            rpcName = "Create${modelledCrud.crudName}Request",
            tableName = modelledCrud.tableToSave.tableName,
            queryName = "${modelledCrud.crudName}Request",
            columns = mutableListOf(
                Column(
                    columnName = modelledCrud.tableToSave.primaryKeyColumn,
                    // This requires the primary column to be named same as the name of the primary key field
                    columnType = modelledCrud.fields.find { modelledCrud.tableToSave.primaryKeyColumn == it.name }?.type!!.name
                ),
            ).plus(
                requestFields.map {
                    if (it.type == RPCTypes.Enum.name) {
                        Column (
                            columnName = it.name,
                            columnType = RPCTypes.String.name + if (it.optional == true) "?" else "",
                            columnValue = RPCTypes.Enum.name
                        )
                    } else {
                        Column(
                            columnName = it.name,
                            columnType = it.type + if (it.optional == true) "?" else "",
                        )
                    }
                },
            ),
            primaryKeyColumn = Column(
                columnName = modelledCrud.tableToSave.primaryKeyColumn
            ),
        ).also {
            generateRPC(it, requestFields, yamlParser.mapper)
            generateServerImpl(it)
        }
    }

    private fun generateRPC(
        createRequestDataModel: CreateRequestDataModel,
        requestFields: List<RequestAndResponseType>,
        mapper: ObjectMapper,
    ) {
        val createRPC = RPC(
            name = createRequestDataModel.rpcName,
            request = requestFields,
            response = null,
        )
        createYaml(
            mapper = mapper,
            path = rpcParentDirectory,
            fileName = createRequestDataModel.rpcName.kebabCase() + "-rpc.yml",
            mapObject = createRPC,
        )
    }
}
