package com.suryadigital.test.amc.queries

import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.IterableResultQuery
import com.suryadigital.leo.basedb.QueryResult

abstract class GetAMCState: IterableResultQuery<GetAMCState.Input,GetAMCState.Result>(){
    data class Input(
        val countryCode: String
    ): QueryInput
    data class Result(
      val key: String,
      val value: String,
    ): QueryResult
}
