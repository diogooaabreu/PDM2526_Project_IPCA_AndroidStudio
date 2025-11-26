package ipca.example.webapi.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

// Define as rotas (ecrãs) que o Bottom Bar irá navegar
sealed class TodoScreen(val route: String, val title: String, val icon: @Composable () -> Unit) {
    // Usar nomes de rotas consistentes e específicos
    object TodoList : TodoScreen("todo_list", "Tarefas", { Icon(Icons.Filled.Home, contentDescription = "Tarefas") })
    object Favorites : TodoScreen("favorites", "Favoritos", { Icon(Icons.Filled.Favorite, contentDescription = "Favoritos") })
}

@Composable
fun TodoBottomBar(
    navController: NavHostController,
    currentRoute: String
) {
    val items = listOf(TodoScreen.TodoList, TodoScreen.Favorites)

    NavigationBar {
        items.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = screen.icon,
                label = { Text(screen.title) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}