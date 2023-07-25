package com.suryadigital.khazana.deal.queries

import com.suryadigital.leo.basedb.NoInputIterableResultQuery
import com.suryadigital.leo.basedb.QueryResult

abstract class GetTransactionType: NoInputIterableResultQuery<GetTransactionType.Result>(){

    data class Result(
      val key: Int,
      val value: String,
    ): QueryResult
}
