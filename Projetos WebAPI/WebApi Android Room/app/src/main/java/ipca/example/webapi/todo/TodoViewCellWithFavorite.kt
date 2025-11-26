package ipca.example.webapi.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ipca.example.webapi.models.Todo

@Composable
fun TodoViewCellWithFavorite(
    todo: Todo,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = todo.title ?: "", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = if (todo.completed == true) "✅ Concluída" else "⏳ Pendente",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Botão/ícone de Favorito
            val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
            val tint = if (isFavorite) Color.Red else Color.Gray

            IconButton(onClick = { todo.id?.let { onFavoriteToggle(it) } }) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
                    tint = tint
                )
            }
        }
    }
}