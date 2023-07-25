package ${packageName}.queries

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import ${packageName?keep_before_last(".")}.jooq.tables.references.${tableName?upper_case}
import com.suryadigital.leo.basedb.DBException

class Insert${queryName}Postgres: Insert${queryName}() {
    override fun implementation(ctx: DSLContext, input: Input) {
         val executedQueryStatus = ctx.insertInto(
            ${tableName?upper_case},
            <#list columns as x>
            ${tableName?upper_case}.${x.columnName?upper_case},
            </#list>
            ).values(
            <#list columns as x>
             input.${x.columnName?uncap_first},
            </#list>
            )
            .execute()
         if (executedQueryStatus <= 0) {
            throw DBException("Values not inserted for input- $input")
        }
    }
}
