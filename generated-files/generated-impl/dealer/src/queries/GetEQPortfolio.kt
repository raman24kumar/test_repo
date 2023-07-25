package com.suryadigital.khazana.dealer.queries

import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.IterableResultQuery
import com.suryadigital.leo.basedb.QueryResult

abstract class GetEQPortfolio: IterableResultQuery<GetEQPortfolio.Input,GetEQPortfolio.Result>(){
    data class Input(
        val id: Int
    ): QueryInput

    data class Result(
      val key: Int,
      val value: String,
    ): QueryResult
}
