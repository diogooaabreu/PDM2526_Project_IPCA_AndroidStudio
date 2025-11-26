package ipca.example.habitslistapp.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ipca.example.habitslistapp.ui.theme.HabitsListAppTheme


@Composable
fun LoginView(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Título
        Text(
            text = "Olá, bem-vindo!\nFaça login para continuar",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        // Campos de entrada
        TextField(
            value = uiState.email ?: "",
            label = { Text("Email") },
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                viewModel.updateEmail(it)
            }
        )

        TextField(
            value = uiState.password ?: "",
            label = { Text("Password") },
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                viewModel.updatePassword(it)
            }
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.error
            )
        }

        // Botão de Login
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                viewModel.login {
                    navController.navigate("homepage")
                }
            }
        ) {
            Text("Login")
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        // Separação visual
        Spacer(modifier = Modifier.height(32.dp))

        // Coluna separada para o botão de registo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ainda não tem conta?",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    navController.navigate("register")
                }
            ) {
                Text("Criar Conta")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    HabitsListAppTheme {
        LoginView(

        )
    }
}

