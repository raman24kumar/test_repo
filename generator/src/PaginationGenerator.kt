package com.suryadigital.eaglegen.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.suryadigital.eaglegen.generator.utils.createRequestResponseOfAttributeFromModel
import com.suryadigital.eaglegen.generator.utils.createYaml
import com.suryadigital.eaglegen.parser.YAMLParser
import com.suryadigital.eaglegen.parser.types.EagleConfiguration
import com.suryadigital.eaglegen.parser.types.ListRPC
import com.suryadigital.eaglegen.parser.types.RPCTypes
import com.suryadigital.eaglegen.parser.types.RequestAndResponseType
import com.suryadigital.eaglegen.parser.types.TypeFile
import com.suryadigital.eaglegen.parser.utils.kebabCase
import com.suryadigital.eaglegen.validator.DataModelCrud
import com.suryadigital.eaglegen.validator.DataModelField
import java.nio.file.Path

class PaginationGenerator(
    private val rpcParentDirectory: Path,
    private val ktParentDirectory: Path,
    private val modelledCrud: DataModelCrud,
    private val appConf: EagleConfiguration,
) {
    fun generate(yamlParser: YAMLParser){
        generateYMLFile(modelledCrud = modelledCrud, mapper = yamlParser.mapper)
    }

    private fun generateYMLFile(modelledCrud: DataModelCrud, mapper: ObjectMapper) {
        val requestFields = mutableListOf<RequestAndResponseType>()
        modelledCrud.fields.filter (DataModelField::isFilterable).map {
            requestFields.add (
                createRequestResponseOfAttributeFromModel(it, optional = true )
            )
        }

        modelledCrud.fields.filter (DataModelField::isSearchable).map {
            requestFields.add (
                createRequestResponseOfAttributeFromModel(it, optional = true )
            )
        }

        requestFields.add(
            RequestAndResponseType(
                name = "itemsPerPage",
                type = RPCTypes.Int32.name
            )
        )
        requestFields.add(
            RequestAndResponseType(
                name = "pageIndex",
                type = RPCTypes.Int32.name
            )
        )
        val responseType = modelledCrud.fields.filter(DataModelField::isMandatory).map {
            createRequestResponseOfAttributeFromModel(it, optional = false)
        }
        createYaml(
            mapper = mapper,
            path = rpcParentDirectory,
            fileName = "Get${modelledCrud.crudName}RequestListResponse".kebabCase()+"-type.yml",
            mapObject = TypeFile(
                name = "Get${modelledCrud.crudName}RequestListResponse",
                attributes = responseType
            )
        )
        val listPRC = ListRPC(
            name = "Get${modelledCrud.crudName}RequestList",
            request = requestFields,
            response = RequestAndResponseType(
                name = "result",
                type = RPCTypes.List.name,
                elementType = "Get${modelledCrud.crudName}RequestListResponse"
            )
        )
        createYaml(
            mapObject = listPRC,
            path = rpcParentDirectory,
            fileName = "Get${modelledCrud.crudName}RequestList".kebabCase()+"-rpc.yml",
            mapper = mapper
        )
    }
}