package com.suryadigital.khazana.dealer

import org.koin.dsl.module
import org.koin.core.module.Module
import com.suryadigital.khazana.dealer.GetEntityLookupRPC
import com.suryadigital.khazana.dealer.GetDealTypeLookupRPC
import com.suryadigital.khazana.dealer.GetDematLookupRPC
import com.suryadigital.khazana.dealer.GetSymbolLookupRPC
import com.suryadigital.khazana.dealer.CreateDealRequestRPC
import com.suryadigital.khazana.dealer.GetPortfolioLookupRPC
import com.suryadigital.khazana.dealer.RoutesModule

val routesModule : Module = module {
   single<GetEntityLookupRPC> { GetEntityLookupRPCServerImpl() }
   single<GetDealTypeLookupRPC> { GetDealTypeLookupRPCServerImpl() }
   single<GetDematLookupRPC> { GetDematLookupRPCServerImpl() }
   single<GetSymbolLookupRPC> { GetSymbolLookupRPCServerImpl() }
   single<CreateDealRequestRPC> { CreateDealRequestRPCServerImpl() }
   single<GetPortfolioLookupRPC> { GetPortfolioLookupRPCServerImpl() }
   single<RoutesModule> { RoutesModule() }
}