package com.suryadigital.test.amc.queries

import com.suryadigital.leo.basedb.SingleResultQuery
import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.QueryResult
import java.util.UUID

abstract class FindAMCRequest: SingleResultQuery<FindAMCRequest.Input, FindAMCRequest.Result>(){
    data class Input(
      val amcId: UUID,
    ): QueryInput

    data class Result(
      val exists: Boolean,
    ): QueryResult
}
