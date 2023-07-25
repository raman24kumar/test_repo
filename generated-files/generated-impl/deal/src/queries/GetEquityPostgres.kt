package com.suryadigital.khazana.deal.queries

import org.jooq.DSLContext
import com.suryadigital.khazana.jooq.tables.references.EQUITY
import com.suryadigital.leo.basedb.getNonNullValue

class GetEquityPostgres: GetEquity() {
    override fun implementation(ctx: DSLContext ): Iterable<Result> {
        return ctx.select(
             EQUITY.ISIN,
             EQUITY.SYMBOL,
        )
        .from(EQUITY)
        .fetch()
        .map {
            Result(
                key = it.getNonNullValue(EQUITY.ISIN),
                value = it.getNonNullValue(EQUITY.SYMBOL),
            )
        }
    }
}
