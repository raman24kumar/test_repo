package ${packageName}

import ${packageName}.${rpcName}RPC.Error
import ${packageName}.${rpcName}RPC.Request
import ${packageName}.${rpcName}RPC.Response
import ${packageName}.exceptions.executeWithExceptionHandling
import ${packageName}.queries.Update${queryName}
import ${packageName}.queries.Find${queryName}
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ${rpcName}RPCServerImpl: ${rpcName}RPC, KoinComponent {
    private val database by inject<Database>()
    private val update${queryName} by inject<Update${queryName}>()
    private val find${queryName} by inject<Find${queryName}>()

    override suspend fun execute(request: Request): LeoRPCResult<Response, Error> {
        return executeWithExceptionHandling {
            LeoRPCResult.response(getResponse(request))
        }
    }

    private suspend fun getResponse(request: Request): Response {
        database.timedQuery(isReadOnly = true){ ctx ->
        if(
            find${queryName}.execute(
               ctx = ctx,
               input = Find${queryName}.Input(
                   ${primaryKeyColumn.columnName} = request.${primaryKeyColumn.columnName},
               )
            ).exists == false
        ){
           throw ${rpcName}RPC.InvalidRequestIdException()
        }
        }
        database.timedQuery { ctx ->
           update${queryName}.execute(
                ctx = ctx,
                input = Update${queryName}.Input(
                    ${primaryKeyColumn.columnName} = request.${primaryKeyColumn.columnName},
                    <#list columns as x>
                    <#if x.columnType == "Enum">
                    ${x.columnName?uncap_first} = request.${x.columnName}.name,
                    <#else>
                    ${x.columnName?uncap_first} = request.${x.columnName},
                    </#if>
                    </#list>
                )
           )
        }
        return Response
    }
}
