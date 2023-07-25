package com.suryadigital.khazana.dealer

import com.suryadigital.khazana.dealer.GetDematLookupRPC.Error
import com.suryadigital.khazana.dealer.GetDematLookupRPC.Request
import com.suryadigital.khazana.dealer.GetDematLookupRPC.Response
import com.suryadigital.khazana.dealer.exceptions.executeWithExceptionHandling
import com.suryadigital.khazana.dealer.queries.GetDematAccount
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetDematLookupRPCServerImpl: GetDematLookupRPC, KoinComponent {
    private val database by inject<Database>()
    private val getDematAccount by inject<GetDematAccount>()

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
        return Response (
            database.timedQuery(isReadOnly = true) { ctx ->
             getDematAccount.execute(
                ctx = ctx,
                input = GetDematAccount.Input(
                    id = request.id
                )
             ).map {
                GetDematLookupResponse(
                    key = it.key,
                    label = it.value
                 )
             }
        }
        )
    }
}
