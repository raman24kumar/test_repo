package com.suryadigital.test.amc

import com.suryadigital.test.amc.GetAMCHoCountryLookupRPC.Error
import com.suryadigital.test.amc.GetAMCHoCountryLookupRPC.Request
import com.suryadigital.test.amc.GetAMCHoCountryLookupRPC.Response
import com.suryadigital.test.amc.exceptions.executeWithExceptionHandling
import com.suryadigital.test.amc.queries.GetAMCCountry
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAMCHoCountryLookupRPCServerImpl: GetAMCHoCountryLookupRPC, KoinComponent {
    private val database by inject<Database>()
    private val getAMCCountry by inject<GetAMCCountry>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse())
        }
    }

    private suspend fun getResponse(): Response {
        return Response (
            database.timedQuery(isReadOnly = true) { ctx ->
                getAMCCountry.execute(
                    ctx = ctx,
                ).map {
                    GetAMCHoCountryLookupResponse(
                        key = it.key,
                        label = it.label
                    )
                }
            }
        )
    }
}
