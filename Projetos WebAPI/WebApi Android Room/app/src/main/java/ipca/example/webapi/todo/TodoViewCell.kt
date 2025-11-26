package ipca.example.webapi.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ipca.example.webapi.models.Todo

@Composable
fun TodoViewCell(todo: Todo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = todo.title?:"", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (todo.completed  == true) "✅ Concluída" else "⏳ Pendente",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
