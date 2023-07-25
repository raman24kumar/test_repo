package ${packageName}

import org.koin.dsl.module
import org.koin.core.module.Module
<#list routesModule as x>
import ${packageName}.${x.first}
</#list>
<#list databaseModule as x>
import ${packageName}.queries.${x.first}
import ${packageName}.queries.${x.second}
</#list>

val ${folderName}Module : Module = module {
   // Routes Module Definition
   <#list routesModule as x>
   single<${x.first}> { ${x.second}() }
   </#list>

   // Database Module Definition
   <#list databaseModule as x>
   single<${x.first}> { ${x.second}() }
   </#list>
}
