package ${packageName}.queries

import org.jooq.DSLContext
import ${packageName?keep_before_last(".")}.jooq.tables.references.${parentTableName?upper_case}
<#list fetchColumns as y>
import ${packageName?keep_before_last(".")}.jooq.tables.references.${y.tableName?upper_case}
</#list>
<#list joinColumns as y>
import ${packageName?keep_before_last(".")}.jooq.tables.references.${y.tableName?upper_case}
</#list>
import com.suryadigital.leo.basedb.getNonNullValue
import org.jooq.impl.DSL

internal class Get${queryName}Postgres: Get${queryName}() {
    override fun implementation(ctx: DSLContext,input: Input): Result {
        val model = ctx.select(
        <#list fetchColumns as y>
        ${y.tableName?upper_case}.${y.columnName?upper_case},
        </#list>
        )
        .from(${parentTableName?upper_case})
        <#list joinColumns as y>
        <#if y.leftJoin>.leftJoin<#else>.innerJoin</#if>(${y.tableName?upper_case}).on(${y.tableName?upper_case}.${y.columnName?upper_case}.eq(${y.referenceTable?upper_case}.${y.referenceColumn?upper_case}))
        </#list>
        .where(
            DSL.trueCondition()
        <#list filterColumn as y>
            .and(input.${y.inputName}?.let{${y.tableName?upper_case}.${y.columnName?upper_case}.eq(input.${y.inputName})} ?: DSL.trueCondition())
        </#list>
        <#list searchableColumns as y>
            .and(input.${y.inputName}?.let{${y.tableName?upper_case}.${y.columnName?upper_case}.likeIgnoreCase(${r'"%${input.'+y.inputName+r'}%"'})} ?: DSL.trueCondition())
        </#list>
        )
        return Result(
        totalItems = ctx.fetchCount(model),
            paginatedList = model
        .offset(input.offset)
        .limit(input.limit)
        .fetch()
        .map {
            PaginatedView(
                <#list fetchColumns as y>
                ${y.inputName} = it.<#if y.optional>get<#else>getNonNullValue</#if>(${y.tableName?upper_case}.${y.columnName?upper_case}),
                </#list>
            )
        },

        )
    }
}
