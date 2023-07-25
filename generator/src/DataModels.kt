package com.suryadigital.eaglegen.generator

import com.suryadigital.eaglegen.parser.types.Type
import java.nio.file.Path

data class MetaModel(
        val folder: Path,
        val fileName: String,
        val templatePath: String,
)

data class LookupModel(
        val metaInfo: List<MetaModel>,
        val packageName: String,
        val dependsOn: Pair<String, Type>?,
        val rpcName: String,
        val tableName: String,
        val keyName: String = "default",
        val valueName: String = "default",
        val columns: List<Column> = listOf(),
        val input: Column? = null,
        val queryName: String,
) : EagleModel {
    override val metaModel: List<MetaModel>
        get() = metaInfo
}

interface EagleModel {
    val metaModel: List<MetaModel>
}

data class Column(
        val columnName: String,
        val columnValue: String = "default",
        val columnType: String = "default",
        val optional: Boolean = false,
)

data class CreateAndUpdateRequestDataModel(
        val metaInfo: List<MetaModel>,
        val packageName: String,
        val rpcName: String,
        val tableName: String,
        val queryName: String,
        val columns: List<Column> = listOf(),
        val primaryKeyColumn: Column,
) : EagleModel {
    override val metaModel: List<MetaModel>
        get() = metaInfo
}

data class QueryStatementModel(
        val inputName: String,
        val inputType: String,
        val tableName: String,
        val columnName: String,
        val optional: Boolean = false,
)

data class ListRequestDataModel(
        val metaInfo: List<MetaModel>,
        val packageName: String,
        val rpcName: String,
        val tableName: String,
        val queryName: String,
        val fetchColumns: List<QueryStatementModel>,
        val itemsPerPage: Int,
        val searchableColumns: List<QueryStatementModel>,
        val filterColumn: List<QueryStatementModel>,
        val joinColumns: List<JoinStatementModel>,
        val parentTableName: String,
) : EagleModel {
    override val metaModel: List<MetaModel>
        get() = metaInfo
}

data class JoinStatementModel(
        val fieldName: String,
        val tableName: String,
        val columnName: String,
        val referenceTable: String,
        val referenceColumn: String,
        val leftJoin: Boolean = false,
)

data class RequestDetailsQueryStatementModel(
        val inputName: String,
        val fetchColumns: List<QueryStatementModel>,
        val derivedType: String? = null,
        val optional: Boolean = false,
)

data class RequestDetailsDataModel(
        val metaInfo: List<MetaModel>,
        val packageName: String,
        val rpcName: String,
        val tableName: String,
        val queryName: String,
        val fetchColumns: List<RequestDetailsQueryStatementModel>,
        val joinColumns: List<JoinStatementModel>,
        val requestIdentifier: Column,
) : EagleModel {
    override val metaModel: List<MetaModel>
        get() = metaInfo
}

data class CreateRequestDataModel(
    val metaInfo: List<MetaModel>,
    val packageName: String,
    val rpcName: String,
    val tableName: String,
    val queryName: String,
    val columns: List<Column> = listOf(),
    val primaryKeyColumn: Column,
) : EagleModel {
    override val metaModel: List<MetaModel>
        get() = metaInfo
}

data class Attribute (
    val name: String,
)