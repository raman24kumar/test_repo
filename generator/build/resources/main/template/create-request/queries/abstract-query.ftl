package ${packageName}.queries

import com.suryadigital.leo.basedb.NoResultQuery
import com.suryadigital.leo.basedb.QueryInput
import java.util.UUID

abstract class Insert${queryName}: NoResultQuery<Insert${queryName}.Input>(){
    data class Input(
    <#list columns as x>
    <#if x.columnType == "Enum">
    val ${x.columnName?uncap_first}: String<#if x.optional>?</#if>,
    <#else>
    val ${x.columnName?uncap_first}: ${x.columnType}<#if x.optional>?</#if>,
    </#if>
    </#list>
    ): QueryInput
}
