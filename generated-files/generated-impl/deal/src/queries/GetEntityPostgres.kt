package com.suryadigital.khazana.deal.queries

import org.jooq.DSLContext
import com.suryadigital.khazana.jooq.tables.references.ENTITY
import com.suryadigital.leo.basedb.getNonNullValue

class GetEntityPostgres: GetEntity() {
    override fun implementation(ctx: DSLContext ): Iterable<Result> {
        return ctx.select(
             ENTITY.ID,
             ENTITY.ENTITYNAME,
        )
        .from(ENTITY)
        .fetch()
        .map {
            Result(
                key = it.getNonNullValue(ENTITY.ID),
                value = it.getNonNullValue(ENTITY.ENTITYNAME),
            )
        }
    }
}
