package com.suryadigital.test.amc

import com.suryadigital.test.amc.CreateAMCRequestRPC.Error
import com.suryadigital.test.amc.CreateAMCRequestRPC.Request
import com.suryadigital.test.amc.CreateAMCRequestRPC.Response
import com.suryadigital.test.amc.exceptions.executeWithExceptionHandling
import com.suryadigital.test.amc.queries.InsertAMCRequest
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class CreateAMCRequestRPCServerImpl: CreateAMCRequestRPC, KoinComponent {
    private val database by inject<Database>()
    private val insertAMCRequest by inject<InsertAMCRequest>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(request))
        }
    }

    private suspend fun getResponse(request: Request): Response {
        database.timedQuery { ctx ->
           insertAMCRequest.execute(
                ctx = ctx,
                input = InsertAMCRequest.Input(
                    amcId = UUID.randomUUID(),
                    amcId = UUID.randomUUID(),
                    amcName = request.amcName,
                    amcManagerName = request.amcManagerName,
                    status = request.status,
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
