package com.suryadigital.eaglegen.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.suryadigital.eaglegen.generator.utils.*
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.types.ErrorType
import com.suryadigital.eaglegen.parser.types.RPC
import com.suryadigital.eaglegen.parser.types.RequestAndResponseType
import com.suryadigital.eaglegen.parser.utils.getRPCType
import com.suryadigital.eaglegen.parser.utils.kebabCase
import com.suryadigital.eaglegen.parser.utils.titleCase
import com.suryadigital.eaglegen.validator.DataModelCrud
import com.suryadigital.eaglegen.validator.DataModelField
import com.suryadigital.eaglegen.validator.DataModelMethod
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name

class RequestDetailsGenerator(
    private val rpcParentDirectory: Path,
    private val ktParentDirectory: Path,
    private val modelledCrud: DataModelCrud,
    private val appConf: EagleConfiguration,
) {
    private val joinList = mutableListOf<JoinStatementModel>()

    fun generate(yamlParser: YAMLParser) {
        generateYMLFile(modelledCrud = modelledCrud, mapper = yamlParser.mapper)
        modelledCrud.fields.filter (DataModelField::isFormField) .map {
            getJoinStatement(modelledCrud, it, joinList)
        }
        val queryStatementModel = mutableListOf<RequestDetailsQueryStatementModel>()
        modelledCrud.fields.filter(DataModelField::isFormField).forEach { field ->
            field.method?.let {
                queryStatementModel.add(
                    RequestDetailsQueryStatementModel(
                        inputName = field.name,
                        fetchColumns = listOf(
                            QueryStatementModel(
                                inputName = field.name + "Key",
                                inputType = (it as DataModelMethod.Lookup).reference.key.type.rpcType.name,
                                tableName = modelledCrud.tableToSave.tableName,
                                columnName = (it as DataModelMethod.Lookup).reference.key.name,
                                optional = field.optional
                            ),
                            QueryStatementModel(
                                inputName = field.name + "Value",
                                inputType = it.reference.label.type.rpcType.name,
                                tableName = modelledCrud.tableToSave.tableName,
                                columnName = it.reference.label.name,
                                optional = field.optional,
                            ),
                        ),
                        derivedType = "Get${modelledCrud.crudName}${field.name.titleCase()}LookupResponse",
                        optional = field.optional,
                    ),
                )
            } ?: queryStatementModel.add(
                RequestDetailsQueryStatementModel(
                    inputName = field.name,
                    fetchColumns = listOf(
                        QueryStatementModel(
                            inputName = field.name,
                            inputType = getEagleTypeFromDataModelField(field).getRPCType().name,
                            tableName = modelledCrud.tableToSave.tableName,
                            columnName = field.name,
                            optional = field.optional,
                        ),
                    ),
                    optional = field.optional,
                ),
            )
        }
        RequestDetailsDataModel(
            metaInfo = listOf(
                MetaModel(
                    folder = Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Get${modelledCrud.crudName}RequestDetails.kt",
                    templatePath = "/request-details/queries/abstract-query.ftl",
                ),
                MetaModel(
                    folder = Paths.get("$ktParentDirectory", "queries"),
                    fileName = "Get${modelledCrud.crudName}RequestDetailsPostgres.kt",
                    templatePath = "/request-details/queries/postgres-query.ftl",
                ),
                MetaModel(
                    ktParentDirectory,
                    fileName = "Get${modelledCrud.crudName}RequestDetailsRPCServerImpl.kt",
                    templatePath = "/request-details/server-impl.ftl",
                ),
            ),
            packageName = "${appConf.packageConfiguration.packageName}.${rpcParentDirectory.name}",
            rpcName = "Get${modelledCrud.crudName}RequestDetails",
            tableName = modelledCrud.tableToSave.tableName,
            fetchColumns = queryStatementModel,
            joinColumns = joinList,
            queryName = "${modelledCrud.crudName}RequestDetails",
            requestIdentifier = Column(
                columnName = modelledCrud.tableToSave.primaryKeyColumn,
                columnType = modelledCrud.fields.find {it.name == modelledCrud.tableToSave.primaryKeyColumn}?.type?.rpcType?.name!!,
            ),
        ).also(::generateServerImpl)
    }

    private fun generateYMLFile(modelledCrud: DataModelCrud, mapper: ObjectMapper) {
        createYaml(
            mapObject = RPC(
                name = "Get${modelledCrud.crudName}RequestDetails",
                request = listOf(
                    RequestAndResponseType(
                        name = modelledCrud.tableToSave.primaryKeyColumn,
                        type = modelledCrud.fields.find { it.name == modelledCrud.tableToSave.primaryKeyColumn }?.type!!.name,
                    ),
                ),
                response = modelledCrud.fields.filter (DataModelField::isFormField).map { field ->
                    if (field.method!=null) {
                        RequestAndResponseType(
                            name = field.name,
                            type = "Get${modelledCrud.crudName}${field.name.titleCase()}LookupResponse",
                            optional = field.optional
                        )
                    } else {
                        createRequestResponseOfAttributeFromModel(field)
                    }
                },
                errors = listOf(
                    ErrorType("INVALID_REQUEST_ID"),
                ),
            ),
            mapper = mapper,
            fileName = "Get${modelledCrud.crudName}RequestDetails".kebabCase() + "-rpc.yml",
            path = rpcParentDirectory,
        )
    }
}
