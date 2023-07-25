package com.suryadigital.khazana.deal.queries

import org.jooq.DSLContext
import com.suryadigital.khazana.jooq.tables.references.EQPORTFOLIO
import com.suryadigital.leo.basedb.getNonNullValue

class GetEQPortfolioPostgres: GetEQPortfolio() {
    override fun implementation(ctx: DSLContext,input: Input ): Iterable<Result> {
        return ctx.select(
             EQPORTFOLIO.ID,
             EQPORTFOLIO.PORTFOLIONAME,
        )
        .from(EQPORTFOLIO).where(EQPORTFOLIO.ENTITYID.eq(input.id))
        .fetch()
        .map {
            Result(
                key = it.getNonNullValue(EQPORTFOLIO.ID),
                value = it.getNonNullValue(EQPORTFOLIO.PORTFOLIONAME),
            )
        }
    }
}
