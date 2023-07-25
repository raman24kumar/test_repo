package com.suryadigital.test.amc.queries

import org.jooq.DSLContext
import com.suryadigital.test.jooq.tables.references.STATE
import com.suryadigital.leo.basedb.getNonNullValue

class GetAMCStatePostgres: GetAMCState() {
    override fun implementation(ctx: DSLContext,input: Input ): Iterable<Result> {
        return ctx.select(
             STATE.STATECODE,
             STATE.STATENAME,
        )
        .from(STATE).where(STATE.COUNTRYCODE.eq(input.countryCode))
        .fetch()
        .map {
            Result(
                key = it.getNonNullValue(STATE.STATECODE),
                value = it.getNonNullValue(STATE.STATENAME),
            )
        }
    }
}
