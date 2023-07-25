package com.suryadigital.test.amc.queries

import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.name
import org.jooq.impl.DSL.table
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.leo.basedb.DBException

class UpdateAMCRequestPostgres: UpdateAMCRequest() {
    override fun implementation(ctx: DSLContext, input: Input) {
        val updatedRows = ctx.update(AMCREQUEST)
            .set(AMCREQUEST.AMCNAME, input.amcName)
            .set(AMCREQUEST.AMCMANAGERNAME, input.amcManagerName)
            .set(AMCREQUEST.STATUS, input.status)
            .set(AMCREQUEST.HOADDRESS1, input.hoAddress1)
            .set(AMCREQUEST.HOADDRESS2, input.hoAddress2)
            .set(AMCREQUEST.HOADDRESS3, input.hoAddress3)
            .set(AMCREQUEST.HOCOUNTRY, input.hoCountry)
            .set(AMCREQUEST.HOSTATE, input.hoState)
            .set(AMCREQUEST.REMARKS, input.remarks)
            .where(AMCREQUEST.AMCID.eq(input.amcId))
            .execute()
        if(updatedRows !=1){
            throw DBException("The data was not updated for the $input")
        }
    }
}
