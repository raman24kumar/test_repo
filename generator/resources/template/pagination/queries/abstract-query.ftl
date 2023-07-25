package ${packageName}.queries

import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.SingleResultQuery
import com.suryadigital.leo.basedb.QueryResult
import java.util.UUID

<#include "/get-type.ftl">

internal abstract class Get${queryName}: SingleResultQuery<Get${queryName}.Input,Get${queryName}.Result>(){
    data class Input(
        <#list filterColumn as y>
        <@getTypeForDataClass y/>?,
        </#list>
        <#list searchableColumns as y>
        <@getTypeForDataClass y/>?,
        </#list>
        val limit: Int,
        val offset: Int,
    ): QueryInput

    data class Result(
        val paginatedList: List<PaginatedView>,
        val totalItems: Int,
    ): QueryResult

    data class PaginatedView(
        <#list fetchColumns as y>
        <@getTypeForDataClass y/><#if y.optional>?</#if>,
        </#list>
    )
}
