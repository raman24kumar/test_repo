package ${packageName}

import ${packageName}.${rpcName}RPC
import ${packageName}.${rpcName}RPC.Request
import ${packageName}.${rpcName}RPC.Response
import ${packageName}.${rpcName}RPC.Error
import ${packageName}.${rpcName}RPC.InvalidRequestIdException
import ${packageName}.queries.Get${queryName}
import ${packageName}.exceptions.executeWithExceptionHandling
import com.suryadigital.leo.basedb.Database
import com.suryadigital.leo.basedb.timedQuery
import com.suryadigital.leo.rpc.LeoRPCResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
<#list fetchColumns as y>
<#if y.derivedType??>
import ${packageName}.${y.derivedType}
</#if>
</#list>

class ${rpcName}RPCServerImpl: ${rpcName}RPC, KoinComponent  {
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
                    ${requestIdentifier.columnName} = request.${requestIdentifier.columnName},
                )
             )?.let { result ->
                Response (
                   <#list fetchColumns as x>
                   <#if x.derivedType??>
                    ${x.inputName} = <#if x.optional>result.${x.inputName}Key?.let{
                     ${x.derivedType}(
                         key = it,
                         label = result.${x.inputName}Value ?: throw IllegalArgumentException("The field ${x.inputName}Value is null"),
                     )
                    }<#else>
                   ${x.derivedType}(
                         key = result.${x.inputName}Key,
                         label = result.${x.inputName}Value,
                     )
                    </#if>,
                   <#else>
                   <#list x.fetchColumns as y>
                   <#if y.inputType == "Enum">
                   ${y.inputName} = Response.${y.inputName?cap_first}.valueOf(result.${y.inputName}),
                   <#else>
                   ${y.inputName} = result.${y.inputName},
                   </#if>
                   </#list>
                   </#if>
                   </#list>
                )
             } ?: throw InvalidRequestIdException()
        }
    }
}
