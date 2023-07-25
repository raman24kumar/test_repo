package com.suryadigital.khazana.deal.queries

import org.koin.dsl.module
import org.koin.core.module.Module

val databaseModule : Module = module {
   single<InsertEQDealRequest> { InsertEQDealRequestPostgres() }
   single<GetEQPortfolio> { GetEQPortfolioPostgres() }
   single<DatabaseModule> { DatabaseModulePostgres() }
   single<GetTransactionType> { GetTransactionTypePostgres() }
   single<GetEntity> { GetEntityPostgres() }
   single<GetDematAccount> { GetDematAccountPostgres() }
   single<GetEquity> { GetEquityPostgres() }
}