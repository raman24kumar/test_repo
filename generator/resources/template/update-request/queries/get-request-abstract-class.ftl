package ${packageName}.queries

import com.suryadigital.leo.basedb.SingleResultQuery
import com.suryadigital.leo.basedb.QueryInput
import com.suryadigital.leo.basedb.QueryResult
import java.util.UUID

abstract class Find${queryName}: SingleResultQuery<Find${queryName}.Input, Find${queryName}.Result>(){
    data class Input(
      val ${primaryKeyColumn.columnName}: ${primaryKeyColumn.columnType},
    ): QueryInput

    data class Result(
      val exists: Boolean,
    ): QueryResult
}
