<#macro getTypeForDataClass y>
<#if y.inputType == "Enum">
val ${y.inputName} : String<#rt>
<#else>
val ${y.inputName} : ${y.inputType}<#rt>
</#if>
</#macro>


<#macro getTypeForServerImplRequest y>
<#if y.inputType == "Enum">
${y.inputName} = request.${y.inputName}<#if y.optional>?</#if>.name<#rt>
<#else>
${y.inputName} = request.${y.inputName}<#rt>
</#if>
</#macro>

<#macro getTypeForServerImplResponse y>
<#if y.inputType == "Enum">
${y.inputName} = ${rpcName}Response.${y.inputName?cap_first}.valueOf(result.${y.inputName})<#rt>
<#else>
${y.inputName} = result.${y.inputName}<#rt>
</#if>
</#macro>
