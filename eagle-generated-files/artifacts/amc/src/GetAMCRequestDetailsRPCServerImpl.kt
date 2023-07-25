package com.suryadigital.test.amc

import com.suryadigital.test.amc.GetAMCRequestDetailsRPC
import com.suryadigital.test.amc.GetAMCRequestDetailsRPC.Request
import com.suryadigital.test.amc.GetAMCRequestDetailsRPC.Response
import com.suryadigital.test.amc.GetAMCRequestDetailsRPC.Error
import com.suryadigital.test.amc.GetAMCRequestDetailsRPC.InvalidRequestIdException
import com.suryadigital.test.amc.queries.GetAMCRequestDetails
import com.suryadigital.test.amc.exceptions.executeWithExceptionHandling
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.suryadigital.test.amc.GetAMCHoCountryLookupResponse
import com.suryadigital.test.amc.GetAMCHoStateLookupResponse

class GetAMCRequestDetailsRPCServerImpl: GetAMCRequestDetailsRPC, KoinComponent  {
    private val database by inject<Database>()
    private val getAMCRequestDetails by inject<GetAMCRequestDetails>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(request))
        }
    }

    private suspend fun getResponse(request: Request): Response {
        return database.timedQuery(isReadOnly = true) { ctx ->
             getAMCRequestDetails.execute(
                ctx = ctx,
                input = GetAMCRequestDetails.Input(
                    amcId = request.amcId,
                )
             )?.let { result ->
                Response (
                   amcName = result.amcName,
                   amcManagerName = result.amcManagerName,
                   status = Response.Status.valueOf(result.status),
                   hoAddress1 = result.hoAddress1,
                   hoAddress2 = result.hoAddress2,
                   hoAddress3 = result.hoAddress3,
                    hoCountry = result.hoCountryKey?.let{
                     GetAMCHoCountryLookupResponse(
                         key = it,
                         label = result.hoCountryValue ?: throw IllegalArgumentException("The field hoCountryValue is null"),
                     )
                    },
                    hoState = result.hoStateKey?.let{
                     GetAMCHoStateLookupResponse(
                         key = it,
                         label = result.hoStateValue ?: throw IllegalArgumentException("The field hoStateValue is null"),
                     )
                    },
                   remarks = result.remarks,
                )
             } ?: throw InvalidRequestIdException()
        }
    }
}
