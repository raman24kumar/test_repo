package ${packageName}.queries

<#if dependsOn?has_content>
import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.IterableResultQuery
<#else>
import com.suryadigital.leo.basedb.NoInputIterableResultQuery
</#if>
import com.suryadigital.leo.basedb.QueryResult

abstract class Get${queryName}: <#if dependsOn?has_content>IterableResultQuery<Get${queryName}.Input,Get${queryName}.Result>()<#else>NoInputIterableResultQuery<Get${queryName}.Result>()</#if>{
    <#if dependsOn?has_content>
    data class Input(
        val ${dependsOn.first?uncap_first}: ${dependsOn.second}
    ): QueryInput
    </#if>
    data class Result(
    <#list columns as x>
      val ${x.columnName?uncap_first}: ${x.columnType},
    </#list>
    ): QueryResult
}
