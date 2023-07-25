package ${packageName}.queries

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import ${packageName?keep_before_last(".")}.jooq.tables.references.${tableName?upper_case}

class Find${queryName}Postgres: Find${queryName}() {
    override fun implementation(ctx: DSLContext, input: Input) : Result {
        return Result(
            exists = ctx.fetchExists(
                ctx.select(${tableName?upper_case}.${primaryKeyColumn.columnName?upper_case})
                   .from(${tableName?upper_case})
                   .where(${tableName?upper_case}.${primaryKeyColumn.columnName?upper_case}.eq(input.${primaryKeyColumn.columnName}))
            )
        )
    }
}
