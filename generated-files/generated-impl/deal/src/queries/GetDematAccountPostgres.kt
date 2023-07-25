package com.suryadigital.khazana.deal.queries

import org.jooq.DSLContext
import com.suryadigital.khazana.jooq.tables.references.DEMATACCOUNT
import com.suryadigital.leo.basedb.getNonNullValue

class GetDematAccountPostgres: GetDematAccount() {
    override fun implementation(ctx: DSLContext,input: Input ): Iterable<Result> {
        return ctx.select(
             DEMATACCOUNT.DEMATACCOUNTNUMBER,
             DEMATACCOUNT.DEMATACCOUNTNUMBER,
        )
        .from(DEMATACCOUNT).where(DEMATACCOUNT.PORTFOLIOID.eq(input.id))
        .fetch()
        .map {
            Result(
                key = it.getNonNullValue(DEMATACCOUNT.DEMATACCOUNTNUMBER),
                value = it.getNonNullValue(DEMATACCOUNT.DEMATACCOUNTNUMBER),
            )
        }
    }
}
