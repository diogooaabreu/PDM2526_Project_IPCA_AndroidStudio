package ipca.example.webapi.todo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ipca.example.webapi.view.TodoDetailView

@Composable
fun FavoritesView(
    navController: NavHostController,
    viewModel: TodoDBListViewModel = viewModel() // ViewModel partilhado
) {
    val uiState = viewModel.uiState.value
    val favoriteTodos = viewModel.getFavoriteTodos() // Lista apenas com os favoritos

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        if (uiState.selectedTodo != null && favoriteTodos.any { it.id == uiState.selectedTodo.id }) {
            // Se um favorito for selecionado DENTRO desta vista, mostrar detalhes
            val selectedTodo = uiState.selectedTodo!!
            val isFavorite = true // No ecrã de favoritos, é sempre favorito

            TodoDetailView(
                todo = selectedTodo,
                isFavorite = isFavorite,
                onBack = {
                    viewModel.selectTodo(null) // Voltar à lista de favoritos
                },
                onFavoriteToggle = { todoId ->
                    viewModel.toggleFavorite(todoId)
                    // Se remover o último favorito, voltar à lista
                    if (viewModel.getFavoriteTodos().isEmpty()) {
                        viewModel.selectTodo(null)
                    }
                }
            )
        } else if (favoriteTodos.isEmpty()) {
            Text(text = "Não tem tarefas favoritas.", modifier = Modifier.align(Alignment.Center))
        } else {
            // Mostrar a lista de Tarefas Favoritas
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favoriteTodos, key = { it.id }) { todo ->
                    TodoViewCellWithFavorite(
                        todo = todo,
                        isFavorite = true, // É favorito
                        onClick = {
                            viewModel.selectTodo(todo) // Selecionar para mostrar detalhes
                        },
                        onFavoriteToggle = { todoId ->
                            viewModel.toggleFavorite(todoId) // Remover dos favoritos
                        }
                    )
                }
            }
        }
    }
}