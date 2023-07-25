package com.suryadigital.khazana.dealer.queries

import org.jooq.DSLContext
import com.suryadigital.khazana.jooq.tables.references.TRANSACTIONTYPE
import com.suryadigital.leo.basedb.getNonNullValue

class GetTransactionTypePostgres: GetTransactionType() {
    override fun implementation(ctx: DSLContext ): Iterable<Result> {
        return ctx.select(
             TRANSACTIONTYPE.ID,
             TRANSACTIONTYPE.TYPE,
        )
        .from(TRANSACTIONTYPE)
        .fetch()
        .map {
            Result(
                key = it.getNonNullValue(TRANSACTIONTYPE.ID),
                value = it.getNonNullValue(TRANSACTIONTYPE.TYPE),
            )
        }
    }
}
