package com.suryadigital.khazana.dealer.queries

import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.IterableResultQuery
import com.suryadigital.leo.basedb.QueryResult

abstract class GetDematAccount: IterableResultQuery<GetDematAccount.Input,GetDematAccount.Result>(){
    data class Input(
        val id: Int
    ): QueryInput

    data class Result(
      val key: String,
      val value: String,
    ): QueryResult
}
