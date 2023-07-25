package com.suryadigital.test.amc

import com.suryadigital.test.amc.GetAMCHoStateLookupRPC.Error
import com.suryadigital.test.amc.GetAMCHoStateLookupRPC.Request
import com.suryadigital.test.amc.GetAMCHoStateLookupRPC.Response
import com.suryadigital.test.amc.exceptions.executeWithExceptionHandling
import com.suryadigital.test.amc.queries.GetAMCState
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAMCHoStateLookupRPCServerImpl: GetAMCHoStateLookupRPC, KoinComponent {
    private val database by inject<Database>()
    private val getAMCState by inject<GetAMCState>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(request))
        }
    }

    private suspend fun getResponse(request: Request): Response {
        return Response (
            database.timedQuery(isReadOnly = true) { ctx ->
                getAMCState.execute(
                    ctx = ctx,
                    input = GetAMCState.Input(countryCode = request.countryCode)
                ).map {
                    GetAMCHoStateLookupResponse(
                        key = it.key,
                        label = it.label
                    )
                }
            }
        )
    }
}
