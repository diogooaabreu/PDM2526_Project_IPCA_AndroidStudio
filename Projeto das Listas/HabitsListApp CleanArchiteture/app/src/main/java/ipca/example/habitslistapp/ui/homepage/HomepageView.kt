package ipca.example.habitslistapp.ui.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ipca.example.habitslistapp.ui.models.Habit

@Composable
fun HomepageView(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val viewModel: HabitsViewModel = hiltViewModel()
    val uiState by viewModel.uiState

    var showAddDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var habitToShare by remember { mutableStateOf<Habit?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }
    var emailShare by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadMyHabits()
        viewModel.loadSharedHabits()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(uiState.error!!, modifier = Modifier.align(Alignment.Center))
            } else {
                Column {
                    Text("Meus Hábitos")
                    LazyColumn {
                        items(uiState.myHabits) { habit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        habitToEdit = habit
                                        showEditDialog = true
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(habit.nome)
                                Button(onClick = { viewModel.deleteHabit(habit.id!!) }) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Partilhados comigo")
                    LazyColumn {
                        items(uiState.sharedHabits) { habit ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(habit.nome)
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Dialogs (Add / Edit / Share) ---
    if (showAddDialog) {
        var nome by remember { mutableStateOf("") }
        var descricao by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Adicionar Hábito") },
            text = {
                Column {
                    OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
                    OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addHabit(nome, descricao)
                    showAddDialog = false
                }) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                Button(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showEditDialog && habitToEdit != null) {
        var editNome by remember { mutableStateOf(habitToEdit!!.nome) }
        var editDescricao by remember { mutableStateOf(habitToEdit!!.descricao) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Hábito") },
            text = {
                Column {
                    OutlinedTextField(value = editNome, onValueChange = { editNome = it }, label = { Text("Nome") })
                    OutlinedTextField(value = editDescricao, onValueChange = { editDescricao = it }, label = { Text("Descrição") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateHabit(habitToEdit!!.id!!, editNome, editDescricao)
                    showEditDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showShareDialog && habitToShare != null) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false; emailShare = "" },
            title = { Text("Partilhar Hábito") },
            text = {
                OutlinedTextField(value = emailShare, onValueChange = { emailShare = it }, label = { Text("Email") })
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.shareHabit(habitToShare!!.id!!, emailShare)
                    showShareDialog = false
                    emailShare = ""
                }) {
                    Text("Partilhar")
                }
            },
            dismissButton = {
                Button(onClick = { showShareDialog = false; emailShare = "" }) { Text("Cancelar") }
            }
        )
    }
}
