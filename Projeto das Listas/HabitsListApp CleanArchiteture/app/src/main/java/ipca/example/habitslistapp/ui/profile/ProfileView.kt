package ipca.example.habitslistapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileView(
    viewModel: ProfileViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    onLogout: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current

    // Carrega o utilizador quando o composable é iniciado
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = "Nome: ${uiState.nome ?: "Desconhecido"}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${uiState.email ?: ""}")
            Text(text = "Telemóvel: ${uiState.telemovel ?: ""}")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                viewModel.logout()
                onLogout?.invoke()
            }) {
                Text("Terminar Sessão")
            }
        }
    }
}
