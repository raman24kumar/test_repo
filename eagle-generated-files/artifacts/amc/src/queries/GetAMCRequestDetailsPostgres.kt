package com.suryadigital.test.amc.queries

import org.jooq.DSLContext
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.AMCREQUEST
import com.suryadigital.test.jooq.tables.references.COUNTRY
import com.suryadigital.test.jooq.tables.references.STATE
import com.suryadigital.leo.basedb.getNonNullValue
import com.suryadigital.leo.basedb.fetchOneOrNone
import org.jooq.impl.DSL

class GetAMCRequestDetailsPostgres: GetAMCRequestDetails() {
    override fun implementation(ctx: DSLContext,input: Input): Result? {
        return ctx.select(
          AMCREQUEST.AMCNAME,
          AMCREQUEST.AMCMANAGERNAME,
          AMCREQUEST.STATUS,
          AMCREQUEST.HOADDRESS1,
          AMCREQUEST.HOADDRESS2,
          AMCREQUEST.HOADDRESS3,
          AMCREQUEST.COUNTRYCODE,
          AMCREQUEST.COUNTRYNAME,
          AMCREQUEST.STATECODE,
          AMCREQUEST.STATENAME,
          AMCREQUEST.REMARKS,
        )
        .from(AMCREQUEST)
        .leftJoin(COUNTRY).on(COUNTRY.COUNTRYCODE.eq(AMCREQUEST.HOCOUNTRY))
        .leftJoin(STATE).on(STATE.STATECODE.eq(AMCREQUEST.HOSTATE))
        .where(
        AMCREQUEST.AMCID.eq(input.amcId)
        )
        .fetchOneOrNone()
        ?.map {
            Result(
            amcName = it.getNonNullValue(AMCREQUEST.AMCNAME),
            amcManagerName = it.getNonNullValue(AMCREQUEST.AMCMANAGERNAME),
            status = it.getNonNullValue(AMCREQUEST.STATUS),
            hoAddress1 = it.get(AMCREQUEST.HOADDRESS1),
            hoAddress2 = it.get(AMCREQUEST.HOADDRESS2),
            hoAddress3 = it.get(AMCREQUEST.HOADDRESS3),
            hoCountryKey = it.get(AMCREQUEST.COUNTRYCODE),
            hoCountryValue = it.get(AMCREQUEST.COUNTRYNAME),
            hoStateKey = it.get(AMCREQUEST.STATECODE),
            hoStateValue = it.get(AMCREQUEST.STATENAME),
            remarks = it.get(AMCREQUEST.REMARKS),
            )
        }
    }
}
