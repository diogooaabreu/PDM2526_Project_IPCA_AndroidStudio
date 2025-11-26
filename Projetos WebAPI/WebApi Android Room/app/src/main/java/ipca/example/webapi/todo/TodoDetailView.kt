package ipca.example.webapi.view

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
fun TodoDetailView(
    todo: Todo,
    isFavorite: Boolean, // Parâmetro para o estado atual
    onBack: () -> Unit,
    onFavoriteToggle: (Int) -> Unit // Função para alternar
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Detalhes da Tarefa", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "ID: ${todo.id}")
        Text(text = "Tarefa: ${todo.title}")
        Text(text = "Utilizador: ${todo.userId}")
        // Maneira segura de lidar com booleanos e boolean?:
        val completedText = if (todo.completed == true) {
            "Estado: ✅ Concluída"
        } else {
            "Estado: ⏳ Pendente"
        }

        Text(text = completedText)

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de Favorito
        val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
        val iconTint = if (isFavorite) Color.Red else Color.Gray
        val buttonText = if (isFavorite) "Remover dos Favoritos" else "Adicionar aos Favoritos"

        Button(
            onClick = { todo.id?.let { onFavoriteToggle(it) } }, // Uso seguro do ID
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color(0xFFFEEEEE) else MaterialTheme.colorScheme.primary
            )

        ) {
            Icon(
                imageVector = icon,
                contentDescription = buttonText,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(buttonText, color = if (isFavorite) Color.Red else Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Voltar")
        }
    }
}