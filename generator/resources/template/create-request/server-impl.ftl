package ${packageName}

import ${packageName}.${rpcName}RPC.Error
import ${packageName}.${rpcName}RPC.Request
import ${packageName}.${rpcName}RPC.Response
import ${packageName}.exceptions.executeWithExceptionHandling
import ${packageName}.queries.Insert${queryName}
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID

class ${rpcName}RPCServerImpl: ${rpcName}RPC, KoinComponent {
    private val database by inject<Database>()
    private val insert${queryName} by inject<Insert${queryName}>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(request))
        }
    }

    private suspend fun getResponse(request: Request): Response {
        database.timedQuery { ctx ->
           insert${queryName}.execute(
                ctx = ctx,
                input = Insert${queryName}.Input(
                <#list columns as x>
                <#if primaryKeyColumn.columnName == x.columnName>
                    ${primaryKeyColumn.columnName} = UUID.randomUUID(),
                <#else>
                <#if x.columnType == "Enum">
                    ${x.columnName} = request.${x.columnName}.name,
                <#else>
                    ${x.columnName} = request.${x.columnName},
                </#if>
                </#if>
                </#list>
                )
           )
        }
        return Response
    }
}
