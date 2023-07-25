package ${packageName}.queries

import org.jooq.DSLContext
import ${packageName?keep_before_last(".")}.jooq.tables.references.${tableName?upper_case}
<#list fetchColumns as x>
<#list x.fetchColumns as y>
import ${packageName?keep_before_last(".")}.jooq.tables.references.${y.tableName?upper_case}
</#list>
</#list>
<#list joinColumns as y>
import ${packageName?keep_before_last(".")}.jooq.tables.references.${y.tableName?upper_case}
</#list>
import com.suryadigital.leo.basedb.getNonNullValue
import com.suryadigital.leo.basedb.fetchOneOrNone
import org.jooq.impl.DSL

class Get${queryName}Postgres: Get${queryName}() {
    override fun implementation(ctx: DSLContext,input: Input): Result? {
        return ctx.select(
         <#list fetchColumns as x>
         <#list x.fetchColumns as y>
          ${y.tableName?upper_case}.${y.columnName?upper_case},
         </#list>
         </#list>
        )
        .from(${tableName?upper_case})
        <#list joinColumns as y>
        <#if y.leftJoin>.leftJoin<#else>.innerJoin</#if>(${y.tableName?upper_case}).on(${y.tableName?upper_case}.${y.columnName?upper_case}.eq(${y.referenceTable?upper_case}.${y.referenceColumn?upper_case}))
        </#list>
        .where(
        ${tableName?upper_case}.${requestIdentifier.columnName?upper_case}.eq(input.${requestIdentifier.columnName})
        )
        .fetchOneOrNone()
        ?.map {
            Result(
            <#list fetchColumns as x>
            <#list x.fetchColumns as y>
            <#if y.optional>
            ${y.inputName} = it.get(${y.tableName?upper_case}.${y.columnName?upper_case}),
            <#else>
            ${y.inputName} = it.getNonNullValue(${y.tableName?upper_case}.${y.columnName?upper_case}),
            </#if>
            </#list>
            </#list>
            )
        }
    }
}
