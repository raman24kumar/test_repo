package com.suryadigital.khazana.dealer.queries

import com.suryadigital.leo.basedb.NoInputIterableResultQuery
import com.suryadigital.leo.basedb.QueryResult

abstract class GetEntity: NoInputIterableResultQuery<GetEntity.Result>(){

    data class Result(
      val key: Int,
      val value: String,
    ): QueryResult
}
