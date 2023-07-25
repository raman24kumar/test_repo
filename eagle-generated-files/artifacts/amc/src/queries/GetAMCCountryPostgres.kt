package com.suryadigital.test.amc.queries

import org.jooq.DSLContext
import com.suryadigital.test.jooq.tables.references.COUNTRY
import com.suryadigital.leo.basedb.getNonNullValue

class GetAMCCountryPostgres: GetAMCCountry() {
    override fun implementation(ctx: DSLContext ): Iterable<Result> {
        return ctx.select(
             COUNTRY.COUNTRYCODE,
             COUNTRY.COUNTRYNAME,
        )
        .from(COUNTRY)
        .fetch()
        .map {
            Result(
                key = it.getNonNullValue(COUNTRY.COUNTRYCODE),
                value = it.getNonNullValue(COUNTRY.COUNTRYNAME),
            )
        }
    }
}
