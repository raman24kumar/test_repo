package com.suryadigital.khazana.deal.queries

import com.suryadigital.leo.basedb.NoInputIterableResultQuery
import com.suryadigital.leo.basedb.QueryResult

abstract class GetEquity: NoInputIterableResultQuery<GetEquity.Result>(){

    data class Result(
      val key: String,
      val value: String,
    ): QueryResult
}
