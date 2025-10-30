package ipca.example.webapi.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ipca.example.webapi.models.Todo

@Composable
fun TodoDetailView(todo: Todo, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Detalhes da Tarefa", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "ID: ${todo.id}")
        Text(text = "Tarefa: ${todo.todo}")
        Text(text = "Utilizador: ${todo.userId}")
        // Maneira segura de lidar com booleanos e boolean?:
        val completedText = if (todo.completed == true) {
            "Estado: ✅ Concluída"
        } else {
            "Estado: ⏳ Pendente"
        }

        Text(text = completedText)
    }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Voltar")
        }
    }

