package com.suryadigital.khazana.dealer

import com.suryadigital.khazana.dealer.GetSymbolLookupRPC.Error
import com.suryadigital.khazana.dealer.GetSymbolLookupRPC.Request
import com.suryadigital.khazana.dealer.GetSymbolLookupRPC.Response
import com.suryadigital.khazana.dealer.exceptions.executeWithExceptionHandling
import com.suryadigital.khazana.dealer.queries.GetEquity
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetSymbolLookupRPCServerImpl: GetSymbolLookupRPC, KoinComponent {
    private val database by inject<Database>()
    private val getEquity by inject<GetEquity>()

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
             getEquity.execute(
                ctx = ctx,
             ).map {
                GetSymbolLookupResponse(
                    key = it.key,
                    label = it.value
                 )
             }
        }
        )
    }
}
