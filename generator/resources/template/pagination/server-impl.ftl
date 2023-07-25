<#include "/get-type.ftl">
package ${packageName}

import ${packageName}.${rpcName}RPC
import ${packageName}.${rpcName}RPC.Request
import ${packageName}.${rpcName}RPC.Response
import ${packageName}.${rpcName}RPC.Error
import ${packageName}.${rpcName}RPC.InvalidPageIndexException
import ${packageName}.queries.Get${parentTableName}List
import ${packageName}.exceptions.executeWithExceptionHandling
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject



internal class ${rpcName}RPCServerImpl: ${rpcName}RPC, KoinComponent  {
    private val database by inject<Database>()
    private val get${queryName} by inject<Get${queryName}>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(request))
        }
    }

    private suspend fun getResponse(request: Request): Response {
        return database.timedQuery(isReadOnly = true) { ctx ->
             get${queryName}.execute(
                ctx = ctx,
                input = Get${queryName}.Input(
                    <#list filterColumn as y>
                    <@getTypeForServerImplRequest y/>,
                    </#list>
                    <#list searchableColumns as y>
                    <@getTypeForServerImplRequest y/>,
                    </#list>
                    limit = request.itemsPerPage,
                    offset = request.itemsPerPage * request.pageIndex
                )
             ).let {
                if (it.paginatedList.isEmpty() && request.pageIndex != 0) {
                    throw InvalidPageIndexException()
                }
                Response (
                result = it.paginatedList.map{ result ->
                    ${rpcName}Response(
                        <#list fetchColumns as y>
                        <@getTypeForServerImplResponse y/>,
                        </#list>
                     )
                     },
                totalItems = it.totalItems,
               )
             }
        }
    }
}
