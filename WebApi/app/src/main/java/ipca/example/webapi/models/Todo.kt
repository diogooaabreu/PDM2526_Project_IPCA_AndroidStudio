package ipca.example.webapi.models

import org.json.JSONObject

data class Todo(
    var id: Int? = null,
    var todo: String? = null,
    var completed: Boolean? = null,
    var userId: Int? = null
) {
    companion object {
        fun fromJson(json: JSONObject): Todo {
            return Todo(
                id = json.getInt("id"),
                todo = json.getString("todo"),
                completed = json.getBoolean("completed"),
                userId = json.getInt("userId")
            )
        }
    }
}
