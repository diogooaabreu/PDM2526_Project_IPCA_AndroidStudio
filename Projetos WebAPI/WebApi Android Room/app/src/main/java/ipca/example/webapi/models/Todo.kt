package ipca.example.webapi.models

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import org.json.JSONObject
import java.net.URLDecoder
import java.net.URLEncoder

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
) {
    companion object {
        // A lógica 'fromJson' estava incompleta e não é usada aqui,
        // mas mantida por contexto da API.
        fun fromJson(json: JSONObject): Todo {
            return Todo(
                id = json.getInt("id"),
                title = json.getString("todo"), // Mudado de "title" para "todo" (como visto no ViewModel)
                completed = json.getBoolean("completed"),
                userId = json.getInt("userId")
            )
        }
    }
}

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun getAll(): List<Todo>

    // insert & update. Se o ID já existir, substitui a entrada existente.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(todo: Todo)

    @Delete
    fun delete(todo: Todo)
}


fun String.encodeUrl() : String {
    return URLEncoder.encode(this, "UTF-8")
}

fun String.decodeUrl() : String {
    return URLDecoder.decode(this, "UTF-8")
}