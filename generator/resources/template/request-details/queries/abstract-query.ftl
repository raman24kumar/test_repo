package ${packageName}.queries

import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.SingleResultOrNullQuery
import com.suryadigital.leo.basedb.QueryResult
import java.util.UUID

abstract class Get${queryName}: SingleResultOrNullQuery<Get${queryName}.Input,Get${queryName}.Result>(){
    data class Input(
        val ${requestIdentifier.columnName}: ${requestIdentifier.columnType},
    ): QueryInput

    data class Result(
        <#list fetchColumns as x>
        <#list x.fetchColumns as y>
        <#if y.inputType == "Enum">
        val ${y.inputName} : String<#if y.optional>?</#if>,
        <#else>
        val ${y.inputName}: ${y.inputType}<#if y.optional>?</#if>,
        </#if>
        </#list>
        </#list>
    ): QueryResult
}
