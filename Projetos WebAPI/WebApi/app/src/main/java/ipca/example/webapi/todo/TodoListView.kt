package ipca.example.webapi.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ipca.example.webapi.viewmodel.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListView(viewModel: TodoListViewModel) {
    val todos by viewModel.todos.collectAsState()
    val selected by viewModel.selectedTodo.collectAsState()

    Box(modifier = Modifier.padding()) {
        if (selected == null) {
            // Mostrar lista de tarefas
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(todos) { todo ->
                    TodoViewCell(todo) {
                        viewModel.selectTodo(todo)
                    }
                }
            }
        } else {
            // Mostrar detalhes da tarefa selecionada
            TodoDetailView(todo = selected!!) {
                //viewModel.selectTodo(null)
            }
        }
    }

}
