package ${packageName}.queries

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import ${packageName?keep_before_last(".")}.jooq.tables.references.${tableName?upper_case}
import com.suryadigital.leo.basedb.DBException

class Update${queryName}Postgres: Update${queryName}() {
    override fun implementation(ctx: DSLContext, input: Input) {
        val updatedRows = ctx.update(${tableName?upper_case})
            <#list columns as x>
            .set(${tableName?upper_case}.${x.columnName?upper_case}, input.${x.columnName?uncap_first})
            </#list>
            .where(${tableName?upper_case}.${primaryKeyColumn.columnName?upper_case}.eq(input.${primaryKeyColumn.columnName}))
            .execute()
        if(updatedRows !=1){
            throw DBException("The data was not updated for the $input")
        }
    }
}
