package ipca.example.webapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ipca.example.webapi.ui.theme.WebApiTheme
import ipca.example.webapi.view.TodoListView
import ipca.example.webapi.viewmodel.TodoListViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            var navTitle by remember { mutableStateOf("Home") }
            var isHomeScreen by remember { mutableStateOf(true) }

            WebApiTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(navTitle) },
                            navigationIcon = {
                                if (!isHomeScreen) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Voltar"
                                        )
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomAppBar {
                            Text(
                                text = "Barra inferior",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            navTitle = "Página Inicial"
                            isHomeScreen = true
                            TodoListView(
                                viewModel = viewModel<TodoListViewModel>()
                            )
                        }
                        composable("details") {
                            navTitle = "Detalhes"
                            isHomeScreen = false
                            DetailsScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Button(onClick = { onNavigate("details") }) {
        Text("Ir para Detalhes")
    }
}

@Composable
fun DetailsScreen() {
    Text("Ecrã de detalhes")
}

@Composable
fun WebApiTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
