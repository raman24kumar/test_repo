package com.suryadigital.test.amc.queries

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import com.suryadigital.test.jooq.tables.references.AMCREQUEST

class FindAMCRequestPostgres: FindAMCRequest() {
    override fun implementation(ctx: DSLContext, input: Input) : Result {
        return Result(
            exists = ctx.fetchExists(
                ctx.select(AMCREQUEST.AMCID)
                   .from(AMCREQUEST)
                   .where(AMCREQUEST.AMCID.eq(input.amcId))
            )
        )
    }
}
