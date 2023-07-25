package com.suryadigital.eaglegen.generator.utils

import com.suryadigital.eaglegen.generator.JoinStatementModel
import com.suryadigital.eaglegen.generator.Attribute
import com.suryadigital.eaglegen.parser.types.CRUD
import com.suryadigital.eaglegen.parser.types.Method
import com.suryadigital.eaglegen.parser.types.RequestAndResponseType
import com.suryadigital.eaglegen.parser.types.Type
import com.suryadigital.eaglegen.parser.utils.getRPCType
import com.suryadigital.eaglegen.validator.DataModelCrud
import com.suryadigital.eaglegen.validator.DataModelField
import com.suryadigital.eaglegen.validator.DataModelMethod
import com.suryadigital.eaglegen.validator.DataModelType
import kotlin.reflect.KProperty1

fun String.toAttribute(): Attribute {
    return Attribute(
        name = this
    )
}

fun createRequestResponseOfAttribute(
    attribute: Attribute,
    crud: CRUD,
    optional: Boolean? = null,
): RequestAndResponseType {
    return when (val type = getEagleType(attribute, crud)) {
        Type.Enum -> {
            RequestAndResponseType(
                name = attribute.name,
                type = type.getRPCType().name,
                cases = crud.fields.find { field -> field.name == attribute.name }?.options
                    ?: throw IllegalArgumentException("The case values we not found"),
                optional = optional ?: crud.fields.find { field -> field.name == attribute.name }?.optional,
            )
        }
        Type.Amount -> {
            throw IllegalArgumentException("The type is not supported yet.")
        }

        else -> {
            RequestAndResponseType(
                name = attribute.name,
                type = type.getRPCType().name,
                optional = optional ?: crud.fields.find { field -> field.name == attribute.name }?.optional,
            )
        }
    }
}

fun createRequestResponseOfAttributeFromModel(
    field: DataModelField,
    optional: Boolean? = null,
): RequestAndResponseType {
    return when (val type = field.type) {
        is DataModelType.Enum -> {
            RequestAndResponseType(
                name = field.name,
                type = type.rpcType.name,
                cases = type.cases.map (String::toAttribute),
                optional = optional?:field.optional,
            )
        }
//        DataModelType.Amount -> {
//            throw IllegalArgumentException("The type is not supported yet.")
//        }

        else -> {
            RequestAndResponseType(
                name = field.name,
                type = type?.rpcType!!.name,
                optional = optional?:field.optional
            )
        }
    }
}

fun getEagleType(
    it: Attribute,
    crud: CRUD,
): Type {
    return (
            crud.fields.find { field -> field.name == it.name }?.type
                ?: crud.database.fields.find { dbField -> dbField.name == it.name }?.reference?.key?.type
            )
        ?: throw IllegalArgumentException("The type for the given field name ${it.name} was not found.")
}

fun getEagleTypeFromDataModelField(field: DataModelField): Type {
    return field.type?.eagleType ?: (field.method as DataModelMethod.Lookup).reference.key.type.eagleType
}
fun getJoinStatement(modelledCrud: DataModelCrud, dataModelField: DataModelField, joinList: MutableList<JoinStatementModel>) {
    if (dataModelField.method != null) {
        (dataModelField.method as DataModelMethod.Lookup)?.dependsOn?.field?.let { field ->
            modelledCrud.fields.find { it.name == field?.name }?.let { getJoinStatement(modelledCrud, it, joinList) }
            if (joinList.find { it.fieldName == dataModelField.name } == null) {
                joinList.add(
                    JoinStatementModel(
                        fieldName = dataModelField.name,
                        tableName = (dataModelField.method as DataModelMethod.Lookup)?.reference?.tableName!!,
                        columnName = (dataModelField.method as DataModelMethod.Lookup)?.reference?.key?.name!!,
                        referenceTable = modelledCrud.tableToSave.tableName,
                        referenceColumn = dataModelField.columnToSave,
                        leftJoin = field!!.optional,
                    ),
                )
            }
        } ?: (dataModelField.method as DataModelMethod.Lookup).reference?.let {
            if (joinList.find { it.fieldName == dataModelField.name } == null) {
                joinList.add(
                    JoinStatementModel(
                        fieldName = dataModelField.name,
                        tableName = (dataModelField.method as DataModelMethod.Lookup).reference?.tableName!!,
                        columnName = (dataModelField.method as DataModelMethod.Lookup).reference.key.name,
                        referenceTable = modelledCrud.tableToSave.tableName,
                        referenceColumn = dataModelField.columnToSave,
                        leftJoin = dataModelField.optional,
                    ),
                )
            }
        }
    }
}
