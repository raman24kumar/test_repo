package com.suryadigital.test.amc.queries

import com.suryadigital.leo.basedb.NoInputIterableResultQuery
import com.suryadigital.leo.basedb.QueryResult

abstract class GetAMCCountry: NoInputIterableResultQuery<GetAMCCountry.Result>(){
    data class Result(
      val key: String,
      val value: String,
    ): QueryResult
}
