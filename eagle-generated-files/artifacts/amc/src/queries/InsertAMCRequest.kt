package com.suryadigital.test.amc.queries

import com.suryadigital.leo.basedb.NoResultQuery
import com.suryadigital.leo.basedb.QueryInput
import java.util.UUID

abstract class InsertAMCRequest: NoResultQuery<InsertAMCRequest.Input>(){
    data class Input(
    val amcId: UUID,
    val amcId: UUID,
    val amcName: String,
    val amcManagerName: String,
    val status: String,
    val hoAddress1: String?,
    val hoAddress2: String?,
    val hoAddress3: String?,
    val hoCountry: String?,
    val hoState: String?,
    val remarks: String?,
    ): QueryInput
}
