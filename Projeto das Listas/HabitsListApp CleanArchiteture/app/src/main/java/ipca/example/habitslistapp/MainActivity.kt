package ipca.example.habitslistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ipca.example.habitslistapp.ui.homepage.HomepageView
import ipca.example.habitslistapp.ui.login.LoginView
import ipca.example.habitslistapp.ui.profile.ProfileView
import ipca.example.habitslistapp.ui.register.RegisterView
import ipca.example.habitslistapp.ui.theme.HabitsListAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HabitsListAppTheme {
                AppNavigation()
            }
        }
    }
}


/**
 * Composable principal que gere a navegação entre ecrãs da aplicação
 * Controla o fluxo de navegação e a autenticação do utilizador
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Define os diferentes destinos (ecrãs) da aplicação

            composable("login") {
                LoginView(navController = navController)
            }
            composable("register") {
                RegisterView(navController = navController)
            }
            composable("homepage") {
                HomepageView(navController = navController)
            }
            composable("profile") {
                ProfileView(
                    navController = navController,
                    onLogout = {
                        // Lógica para Terminar Sessão e Navegar
                        navController.navigate("login") {
                            // Limpa a backstack: impede que o utilizador volte ao perfil
                            // com o botão 'back' do telemóvel depois de terminar sessão.
                            popUpTo("homepage") {
                                inclusive = true // Remove o ecrã anterior (homepage)
                            }
                            // Certifica-se de que não cria múltiplas cópias do ecrã de login
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }

    // Efeito colateral que executa uma vez quando o composable é criado
    LaunchedEffect(Unit) {
        // Verifica se há um utilizador autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Se o utilizador já estiver autenticado, navega diretamente para a homepage
        if (currentUser != null) {
            navController.navigate("homepage")
        }
    }
}

