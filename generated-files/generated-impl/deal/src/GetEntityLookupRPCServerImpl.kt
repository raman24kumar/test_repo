package com.suryadigital.khazana.deal

import com.suryadigital.khazana.deal.GetEntityLookupRPC.Error
import com.suryadigital.khazana.deal.GetEntityLookupRPC.Request
import com.suryadigital.khazana.deal.GetEntityLookupRPC.Response
import com.suryadigital.khazana.deal.exceptions.executeWithExceptionHandling
import com.suryadigital.khazana.deal.queries.GetEntity
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetEntityLookupRPCServerImpl: GetEntityLookupRPC, KoinComponent {
    private val database by inject<Database>()
    private val getEntity by inject<GetEntity>()

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
             getEntity.execute(
                ctx = ctx,
             ).map {
                GetEntityLookupResponse(
                    key = it.key,
                    label = it.value
                 )
             }
        }
        )
    }
}
