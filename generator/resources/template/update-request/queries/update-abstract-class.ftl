package ${packageName}.queries

import com.suryadigital.leo.basedb.NoResultQuery
import com.suryadigital.leo.basedb.QueryInput
import java.util.UUID

abstract class Update${queryName}: NoResultQuery<Update${queryName}.Input>(){
    data class Input(
      val ${primaryKeyColumn.columnName}: ${primaryKeyColumn.columnType},
    <#list columns as x>
    <#if x.columnType == "Enum">
     val ${x.columnName?uncap_first} : String<#if x.optional>?</#if>,
    <#else>
      val ${x.columnName?uncap_first}: ${x.columnType}<#if x.optional>?</#if>,
    </#if>
    </#list>
    ): QueryInput
}
