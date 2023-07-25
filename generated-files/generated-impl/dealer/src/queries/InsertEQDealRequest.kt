package com.suryadigital.khazana.dealer.queries

import com.suryadigital.leo.basedb.NoResultQuery
import com.suryadigital.leo.basedb.QueryInput

abstract class InsertEQDealRequest: NoResultQuery<InsertEQDealRequest.Input>(){
    data class Input(
      val iSIN: String,
      val dematAccountNumber: String,
      val amount: Long,
      val dealRequestId: Int,
      val quantity: Long,
    ): QueryInput
}
