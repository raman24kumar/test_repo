package com.suryadigital.test.amc.queries

import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.SingleResultOrNullQuery
import com.suryadigital.leo.basedb.QueryResult
import java.util.UUID

abstract class GetAMCRequestDetails: SingleResultOrNullQuery<GetAMCRequestDetails.Input,GetAMCRequestDetails.Result>(){
    data class Input(
        val amcId: UUID,
    ): QueryInput

    data class Result(
        val amcName: String,
        val amcManagerName: String,
        val status : String,
        val hoAddress1: String?,
        val hoAddress2: String?,
        val hoAddress3: String?,
        val hoCountryKey: String?,
        val hoCountryValue: String?,
        val hoStateKey: String?,
        val hoStateValue: String?,
        val remarks: String?,
    ): QueryResult
}
