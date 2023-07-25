package ${packageName}

import ${packageName}.${rpcName}RPC.Error
import ${packageName}.${rpcName}RPC.Request
import ${packageName}.${rpcName}RPC.Response
import ${packageName}.exceptions.executeWithExceptionHandling
import ${packageName}.queries.Get${queryName}
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ${rpcName}RPCServerImpl: ${rpcName}RPC, KoinComponent {
    private val database by inject<Database>()
    private val get${queryName} by inject<Get${queryName}>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(<#if dependsOn?has_content>request</#if>))
        }
    }

    private suspend fun getResponse(<#if dependsOn?has_content>request: Request</#if>): Response {
        return Response (
            database.timedQuery(isReadOnly = true) { ctx ->
                get${queryName}.execute(
                    ctx = ctx,
                    <#if dependsOn?has_content>
                    input = Get${queryName}.Input(${dependsOn.first} = request.${dependsOn.first})
                    </#if>
                ).map {
                    ${rpcName}Response(
                        key = it.key,
                        label = it.label
                    )
                }
            }
        )
    }
}
