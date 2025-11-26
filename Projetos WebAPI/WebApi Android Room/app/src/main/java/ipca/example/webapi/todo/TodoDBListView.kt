package ipca.example.webapi.todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ipca.example.webapi.view.TodoDetailView

// TodoDBListView agora gere a lista e a navegação (interna) para os detalhes
@Composable
fun TodoDBListView(
    navController: NavHostController,
    viewModel: TodoDBListViewModel // ViewModel partilhado
) {
    val uiState = viewModel.uiState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.error != null) {
            Text(text = "Erro: ${uiState.error}", modifier = Modifier.align(Alignment.Center))
        } else if (uiState.selectedTodo != null) {
            // Mostrar Detalhes quando uma tarefa é selecionada
            val selectedTodo = uiState.selectedTodo!!
            val isFavorite = selectedTodo.id?.let { uiState.favoriteTodoIds.contains(it) } ?: false

            TodoDetailView(
                todo = selectedTodo,
                isFavorite = isFavorite,
                onBack = {
                    viewModel.selectTodo(null) // Voltar à lista
                },
                onFavoriteToggle = { todoId ->
                    viewModel.toggleFavorite(todoId)
                }
            )
        } else {
            // Mostrar Lista de Tarefas
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(uiState.todos, key = { it.id }) { todo ->
                    val isFavorite = todo.id?.let { uiState.favoriteTodoIds.contains(it) } ?: false

                    TodoViewCellWithFavorite(
                        todo = todo,
                        isFavorite = isFavorite,
                        onClick = {
                            viewModel.selectTodo(todo) // Selecionar para mostrar detalhes
                        },
                        onFavoriteToggle = { todoId ->
                            viewModel.toggleFavorite(todoId) // Alternar favorito
                        }
                    )
                }
            }
        }
    }
}