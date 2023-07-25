package com.suryadigital.khazana.deal

import org.koin.dsl.module
import org.koin.core.module.Module
import com.suryadigital.khazana.deal.GetEntityLookupRPC
import com.suryadigital.khazana.deal.GetDealTypeLookupRPC
import com.suryadigital.khazana.deal.GetDematLookupRPC
import com.suryadigital.khazana.deal.GetSymbolLookupRPC
import com.suryadigital.khazana.deal.CreateDealRequestRPC
import com.suryadigital.khazana.deal.GetPortfolioLookupRPC
import com.suryadigital.khazana.deal.RoutesModule

val routesModule : Module = module {
   single<GetEntityLookupRPC> { GetEntityLookupRPCServerImpl() }
   single<GetDealTypeLookupRPC> { GetDealTypeLookupRPCServerImpl() }
   single<GetDematLookupRPC> { GetDematLookupRPCServerImpl() }
   single<GetSymbolLookupRPC> { GetSymbolLookupRPCServerImpl() }
   single<CreateDealRequestRPC> { CreateDealRequestRPCServerImpl() }
   single<GetPortfolioLookupRPC> { GetPortfolioLookupRPCServerImpl() }
   single<RoutesModule> { RoutesModule() }
}