package ipca.example.webapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ipca.example.webapi.components.TodoBottomBar
import ipca.example.webapi.ui.theme.WebApiTheme
import ipca.example.webapi.components.TodoScreen
import ipca.example.webapi.todo.FavoritesView
import ipca.example.webapi.todo.TodoDBListView
import ipca.example.webapi.todo.TodoDBListViewModel


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            var navTitle by remember { mutableStateOf("Tarefas") } // Titulo por defeito
            var isHomeScreen by remember { mutableStateOf(true) }

            // Instância ÚNICA do ViewModel para toda a aplicação 
            val sharedViewModel: TodoDBListViewModel = viewModel()

            WebApiTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route
                                ?: "todo_list"

                        // Determinar o título e o estado do botão Voltar
                        navTitle = when (currentRoute) {
                            TodoScreen.TodoList.route -> TodoScreen.TodoList.title
                            TodoScreen.Favorites.route -> TodoScreen.Favorites.title
                            else -> "Detalhes" // Para quando selectedTodo estiver ativo na lista/favoritos
                        }

                        isHomeScreen =
                            currentRoute == TodoScreen.TodoList.route || currentRoute == TodoScreen.Favorites.route


                        TopAppBar(
                            title = { Text(navTitle) },
                            navigationIcon = {
                                if (!isHomeScreen || sharedViewModel.uiState.value.selectedTodo != null) {
                                    IconButton(onClick = {
                                        if (sharedViewModel.uiState.value.selectedTodo != null) {
                                            sharedViewModel.selectTodo(null) // Fechar detalhes
                                        } else {
                                            navController.popBackStack()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Voltar"
                                        )
                                    }
                                }
                            }
                        )
                    },
                    // Usar o componente Bottom Bar
                    bottomBar = {
                        val currentRoute =
                            navController.currentBackStackEntryAsState().value?.destination?.route
                                ?: "todo_list"
                        // Ocultar Bottom Bar se estiver em modo de Detalhes
                        if (sharedViewModel.uiState.value.selectedTodo == null) {
                            TodoBottomBar(navController = navController, currentRoute = currentRoute)
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "todo_list"
                        ) {
                            composable(TodoScreen.TodoList.route) {
                                TodoDBListView(navController = navController, viewModel = sharedViewModel)
                            }
                            composable(TodoScreen.Favorites.route) {
                                FavoritesView(
                                    navController=navController,
                                    viewModel = sharedViewModel // Passar o ViewModel
                                )
                            }
                        }

                    }
                }
            }
        }
    }


    @Composable
    fun WebApiTheme(content: @Composable () -> Unit) {
        MaterialTheme {
            content()
        }
    }
}