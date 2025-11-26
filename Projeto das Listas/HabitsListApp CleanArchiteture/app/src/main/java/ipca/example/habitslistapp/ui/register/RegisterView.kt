package ipca.example.habitslistapp.ui.register


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun RegisterView(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val viewModel: RegisterViewModel = viewModel()
    val uiState by viewModel.uiState

    // Layout em coluna centralizada
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título simples
        Text(
            text = "Crie a sua conta",
            modifier = Modifier.padding(8.dp)
        )


        // Campo para o nome
        TextField(
            value = uiState.nome ?: "",
            label = { Text("Nome") },
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                viewModel.updateName(it)
            }
        )

        // Campo para o telemóvel
        TextField(
            value = uiState.telemovel ?: "",
            label = { Text("Telemóvel") },
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                viewModel.updatePhone(it)
            }
        )

        // Campo de texto para o email
        TextField(
            value = uiState.email ?: "",                      // Mostra o valor atual do email
            label = { Text("Email") },                        // Rótulo do campo
            modifier = Modifier.padding(8.dp),
            onValueChange = {                                 // Quando o utilizador escreve algo
                viewModel.updateEmail(it)                     // Atualiza o estado no ViewModel
            }
        )

        // Campo de texto para a password
        TextField(
            value = uiState.password ?: "",                   // Mostra o valor atual da password
            label = { Text("Password") },                     // Rótulo do campo
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                viewModel.updatePassword(it)                  // Atualiza o estado no ViewModel
            }
        )

        // Campo opcional para confirmar a password
        TextField(
            value = uiState.confirmPassword ?: "",            // Mostra valor atual da confirmação
            label = { Text("Confirmar Password") },
            modifier = Modifier.padding(8.dp),
            onValueChange = {
                viewModel.updateConfirmPassword(it)
            }
        )

        // Mostra o erro (se existir)
        if (uiState.error != null) {
            Text(
                text = uiState.error!!,                       // Mostra a mensagem de erro
                modifier = Modifier.padding(8.dp)
            )
        }

        // Botão de registo
        Button(
            modifier = Modifier.padding(8.dp),
            onClick = {
                // Tenta criar o utilizador no Firebase
                viewModel.register {
                    // Se o registo for bem-sucedido, navega para a página de login
                    navController.navigate("login")
                }
            }
        ) {
            Text("Registar")                                 // Texto do botão
        }

        // Indicador de carregamento (aparece enquanto o Firebase processa o pedido)
        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
    }
}


