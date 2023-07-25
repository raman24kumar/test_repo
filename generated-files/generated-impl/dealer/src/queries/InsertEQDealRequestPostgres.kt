package com.suryadigital.khazana.dealer.queries

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import com.suryadigital.khazana.jooq.tables.references.EQDEALREQUEST

class InsertEQDealRequestPostgres: InsertEQDealRequest() {
    override fun implementation(ctx: DSLContext, input: Input) {
        ctx.insertInto(
            EQDEALREQUEST,
            EQDEALREQUEST.ISIN,
            EQDEALREQUEST.DEMATACCOUNTNUMBER,
            EQDEALREQUEST.AMOUNT,
            EQDEALREQUEST.DEALREQUESTID,
            EQDEALREQUEST.QUANTITY,
            ).values(
             input.iSIN,
             input.dematAccountNumber,
             input.amount,
             input.dealRequestId,
             input.quantity,
            )
            .execute()
    }
}
