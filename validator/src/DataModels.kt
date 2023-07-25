package com.suryadigital.eaglegen.validator

import com.suryadigital.eaglegen.parser.types.RPCTypes
import com.suryadigital.eaglegen.parser.types.Type

data class DataModelCrud(
    val crudName: String,
    val tableToSave: TableToSave,
    val fields: List<DataModelField>
)

data class TableToSave (
    val tableName: String,
    val primaryKeyColumn: String
)

data class DataModelField(
    val name: String,
    val type: DataModelType?,
    val method: DataModelMethod?,
    val optional: Boolean,
    val columnToSave: String,
    val isPrimaryKey: Boolean = false,
    val title: String,
    val isSearchable: Boolean,
    val isFilterable: Boolean,
    val isMandatory: Boolean,
    val isFormField: Boolean // maybe can be added later
)

sealed class DataModelType : Name {
    object String : DataModelType() {
        override val name: kotlin.String
            get() = "String"
        override val rpcType: RPCTypes
            get() = RPCTypes.String
        override val eagleType: Type
            get() = Type.String
    }

    object Int32 : DataModelType() {
        override val name: kotlin.String
            get() = "Int"
        override val rpcType: RPCTypes
            get() = RPCTypes.Int32
        override val eagleType: Type
            get() = Type.Int
    }

    object Int64 : DataModelType() {
        override val name: kotlin.String
            get() = "Long"
        override val rpcType: RPCTypes
            get() = RPCTypes.Int64
        override val eagleType: Type
            get() = Type.Long
    }

    data class Enum(
        val cases: kotlin.collections.List<kotlin.String>
    ) : DataModelType() {
        override val name: kotlin.String
            get() = "Enum"
        override val rpcType: RPCTypes
            get() = RPCTypes.Enum
        override val eagleType: Type
            get() = Type.Enum
    }

    object List : DataModelType() {
        override val name: kotlin.String
            get() = "List"
        override val rpcType: RPCTypes
            get() = RPCTypes.List
        override val eagleType: Type
            get() = throw IllegalStateException("Type has not been defined")
    }

    object UUID : DataModelType() {
        override val name: kotlin.String
            get() = "UUID"
        override val rpcType: RPCTypes
            get() = RPCTypes.UUID
        override val eagleType: Type
            get() = Type.UUID
    }
}

interface Name {
    val name: String
    val rpcType: RPCTypes
    val eagleType : Type
}

sealed class DataModelMethod {
    data class Lookup(
        val reference: Reference,
        val dependsOn: DependsOn?
    ) : DataModelMethod()

    data class DependsOn(
        val field: DataModelField,
        val column: String
    )

    data class Reference(
        val tableName: String,
        val key: Attribute,
        val label: Attribute
    )

    data class Attribute(
        val name: String,
        val type: DataModelType
    )

    data class TableToSave(
        val tableName: String,
        val primaryKeyColumn: String
    )
}
