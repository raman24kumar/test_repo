package com.suryadigital.test.amc

import com.suryadigital.test.amc.UpdateAMCRequestRPC.Error
import com.suryadigital.test.amc.UpdateAMCRequestRPC.Request
import com.suryadigital.test.amc.UpdateAMCRequestRPC.Response
import com.suryadigital.test.amc.exceptions.executeWithExceptionHandling
import com.suryadigital.test.amc.queries.UpdateAMCRequest
import com.suryadigital.test.amc.queries.FindAMCRequest
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateAMCRequestRPCServerImpl: UpdateAMCRequestRPC, KoinComponent {
    private val database by inject<Database>()
    private val updateAMCRequest by inject<UpdateAMCRequest>()
    private val findAMCRequest by inject<FindAMCRequest>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(request))
        }
    }

    private suspend fun getResponse(request: Request): Response {
        database.timedQuery(isReadOnly = true){ ctx ->
        if(
            findAMCRequest.execute(
               ctx = ctx,
               input = FindAMCRequest.Input(
                   amcId = request.amcId,
               )
            ).exists == false
        ){
           throw UpdateAMCRequestRPC.InvalidRequestIdException()
        }
        }
        database.timedQuery { ctx ->
           updateAMCRequest.execute(
                ctx = ctx,
                input = UpdateAMCRequest.Input(
                    amcId = request.amcId,
                    amcName = request.amcName,
                    amcManagerName = request.amcManagerName,
                    status = request.status.name,
                    hoAddress1 = request.hoAddress1,
                    hoAddress2 = request.hoAddress2,
                    hoAddress3 = request.hoAddress3,
                    hoCountry = request.hoCountry,
                    hoState = request.hoState,
                    remarks = request.remarks,
                )
           )
        }
        return Response
    }
}
