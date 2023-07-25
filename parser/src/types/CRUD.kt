package com.suryadigital.eaglegen.parser.types

data class CRUD(
    val name: String,
    val fields: List<Field>,
    val database: DB,
    val webUI: WebUI,
)

data class Field(
    val name: String,
    val method: Method?,
    val type: Type?,
    val options: List<Attribute>?,
    val optional: Boolean?,
)

data class DB(
    val tableToSave: TableSave,
    val fields: List<DBFields>,
)

data class TableSave(
    val table: String,
    val primaryKeyColumn: String,
)

data class DBFields(
    val name: String,
    val column: String,
    val reference: Reference?,
    val dependsOn: Depends?,
)

data class Depends(
    val field: String,
    val column: String,
)

data class Reference(
    val table: String,
    val key: ReferenceFields,
    val value: ReferenceFields,
)

data class ReferenceFields(
    val column: String,
    val type: Type,
)

data class WebUI(
    val textMap: List<TextMapField>,
    val form: List<Attribute>,
    val table: Table,
)

data class Table(
    val searchableAttributes: List<Attribute>,
    val filterableAttributes: List<Attribute>,
    val columns: List<Attribute>,
    val itemsPerPage: Int
)

data class TextMapField(
    val name: String,
    val title: String,
)

data class Attribute(
    val name: String,
)
