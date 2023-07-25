package com.suryadigital.test.amc.queries

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.leo.basedb.DBException

class InsertAMCRequestPostgres: InsertAMCRequest() {
    override fun implementation(ctx: DSLContext, input: Input) {
         val executedQueryStatus = ctx.insertInto(
            AMCREQUEST,
            AMCREQUEST.AMCID,
            AMCREQUEST.AMCID,
            AMCREQUEST.AMCNAME,
            AMCREQUEST.AMCMANAGERNAME,
            AMCREQUEST.STATUS,
            AMCREQUEST.HOADDRESS1,
            AMCREQUEST.HOADDRESS2,
            AMCREQUEST.HOADDRESS3,
            AMCREQUEST.HOCOUNTRY,
            AMCREQUEST.HOSTATE,
            AMCREQUEST.REMARKS,
            ).values(
             input.amcId,
             input.amcId,
             input.amcName,
             input.amcManagerName,
             input.status,
             input.hoAddress1,
             input.hoAddress2,
             input.hoAddress3,
             input.hoCountry,
             input.hoState,
             input.remarks,
            )
            .execute()
         if (executedQueryStatus <= 0) {
            throw DBException("Values not inserted for input- $input")
        }
    }
}
