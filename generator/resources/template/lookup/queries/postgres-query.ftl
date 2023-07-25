package ${packageName}.queries

import org.jooq.DSLContext
import ${packageName?keep_before_last(".")}.jooq.tables.references.${tableName?upper_case}
import com.suryadigital.leo.basedb.getNonNullValue

class Get${queryName}Postgres: Get${queryName}() {
    override fun implementation(ctx: DSLContext<#if dependsOn?has_content>,input: Input</#if> ): Iterable<Result> {
        return ctx.select(
            <#list columns as x>
             ${tableName?upper_case}.${x.columnValue?upper_case},
            </#list>
        )
        .from(${tableName?upper_case})<#if dependsOn?has_content>.where(${tableName?upper_case}.${dependsOn.first?upper_case}.eq(input.${dependsOn.first}))</#if>
        .fetch()
        .map {
            Result(
                <#list columns as x>
                ${x.columnName} = it.getNonNullValue(${tableName?upper_case}.${x.columnValue?upper_case}),
                </#list>
            )
        }
    }
}
