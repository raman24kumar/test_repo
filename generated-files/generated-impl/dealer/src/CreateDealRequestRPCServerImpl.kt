package com.suryadigital.khazana.dealer

import com.suryadigital.khazana.dealer.CreateDealRequestRPC.Error
import com.suryadigital.khazana.dealer.CreateDealRequestRPC.Request
import com.suryadigital.khazana.dealer.CreateDealRequestRPC.Response
import com.suryadigital.khazana.dealer.exceptions.executeWithExceptionHandling
import com.suryadigital.khazana.dealer.queries.InsertEQDealRequest
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CreateDealRequestRPCServerImpl: CreateDealRequestRPC, KoinComponent {
    private val database by inject<Database>()
    private val insertEQDealRequest by inject<InsertEQDealRequest>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(
                    request
            ))
        }
    }

    private suspend fun getResponse(
    request: Request
    ): Response {
        database.timedQuery { ctx ->
           insertEQDealRequest.execute(
                ctx = ctx,
                input = InsertEQDealRequest.Input(
                    iSIN = request.symbol,
                    dematAccountNumber = request.demat,
                    amount = request.amount,
                    dealRequestId = request.dealType,
                    quantity = request.quantity,
                )
           )
        }
        return Response
    }
}
