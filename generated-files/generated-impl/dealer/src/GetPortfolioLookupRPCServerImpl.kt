package com.suryadigital.khazana.dealer

import com.suryadigital.khazana.dealer.GetPortfolioLookupRPC.Error
import com.suryadigital.khazana.dealer.GetPortfolioLookupRPC.Request
import com.suryadigital.khazana.dealer.GetPortfolioLookupRPC.Response
import com.suryadigital.khazana.dealer.exceptions.executeWithExceptionHandling
import com.suryadigital.khazana.dealer.queries.GetEQPortfolio
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetPortfolioLookupRPCServerImpl: GetPortfolioLookupRPC, KoinComponent {
    private val database by inject<Database>()
    private val getEQPortfolio by inject<GetEQPortfolio>()

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
             getEQPortfolio.execute(
                ctx = ctx,
                input = GetEQPortfolio.Input(
                    id = request.id
                )
             ).map {
                GetPortfolioLookupResponse(
                    key = it.key,
                    label = it.value
                 )
             }
        }
        )
    }
}
