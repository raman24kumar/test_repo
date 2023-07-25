package com.suryadigital.khazana.dealer

import com.suryadigital.khazana.dealer.GetDealTypeLookupRPC.Error
import com.suryadigital.khazana.dealer.GetDealTypeLookupRPC.Request
import com.suryadigital.khazana.dealer.GetDealTypeLookupRPC.Response
import com.suryadigital.khazana.dealer.exceptions.executeWithExceptionHandling
import com.suryadigital.khazana.dealer.queries.GetTransactionType
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetDealTypeLookupRPCServerImpl: GetDealTypeLookupRPC, KoinComponent {
    private val database by inject<Database>()
    private val getTransactionType by inject<GetTransactionType>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(
            ))
        }
    }

    private suspend fun getResponse(
    ): Response {
        return Response (
            database.timedQuery(isReadOnly = true) { ctx ->
             getTransactionType.execute(
                ctx = ctx,
             ).map {
                GetDealTypeLookupResponse(
                    key = it.key,
                    label = it.value
                 )
             }
        }
        )
    }
}
