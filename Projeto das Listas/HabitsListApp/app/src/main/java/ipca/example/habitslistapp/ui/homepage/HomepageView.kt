package ipca.example.habitslistapp.ui.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun HomepageView(
    viewModel: HabitsViewModel = viewModel(),
    navController: NavController,
) {
    //  ESTADOS ATUALIZADOS: Usar myHabits e sharedHabits
    val myHabits by viewModel.myHabits.collectAsState()
    val sharedHabits by viewModel.sharedHabits.collectAsState()

    // ESTADOS DE PARTILHA ADICIONADOS
    var showShareDialog by remember { mutableStateOf(false) }
    var habitToShare by remember { mutableStateOf<Habit?>(null) }
    var emailShare by remember { mutableStateOf("") }


    // Estados dos dialog
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null)}

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Hábito")
            }
        }
    ) {innerPadding ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Hábitos", style = MaterialTheme.typography.headlineMedium)
                Button(
                    onClick = { navController.navigate("profile") },
                ) {
                    Text("Ver Perfil")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- LISTA 1: MEUS HÁBITOS (Criados por mim) ---

            Text("Meus Hábitos", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(myHabits) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(habit.nome, style = MaterialTheme.typography.titleMedium)
                            Text(habit.descricao, style = MaterialTheme.typography.bodyMedium)

                            // Mostrar lista de partilha (opcional)
                            if (habit.partilhadoCom.isNotEmpty()) {
                                Text("Partilhado com: ${habit.partilhadoCom.joinToString()}", style = MaterialTheme.typography.bodySmall)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(onClick = { habit.id?.let { viewModel.deleteHabit(it) } }) {
                                    Text("Eliminar")
                                }

                                Button(onClick = {
                                    habitToEdit = habit
                                    showEditDialog = true
                                }) {
                                    Text("Editar")
                                }

                                //  BOTÃO DE PARTILHA ADICIONADO
                                Button(onClick = {
                                    habitToShare = habit
                                    showShareDialog = true
                                }) {
                                    Text("Partilhar")
                                }
                            }
                        }
                    }
                }
            }

            // --- LISTA 2: HÁBITOS PARTILHADOS COMIGO ---
            Spacer(modifier = Modifier.height(24.dp))
            Text("Partilhados Comigo", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(sharedHabits) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) // Cor diferente
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(habit.nome, style = MaterialTheme.typography.titleMedium)
                            Text(habit.descricao, style = MaterialTheme.typography.bodyMedium)
                            Text("Criado por: ${habit.criadoPor}", style = MaterialTheme.typography.bodySmall)
                            // Não permitir eliminar/editar hábitos criados por outros
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espaço para o FAB

            // dialog para adicionar hábito
            if (showAddDialog) {
                var nome by remember { mutableStateOf("") }
                var descricao by remember { mutableStateOf("") }

                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text("Adicionar Hábito") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = nome,
                                onValueChange = { nome = it },
                                label = { Text("Nome do Hábito") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = descricao,
                                onValueChange = { descricao = it },
                                label = { Text("Descrição") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (nome.isNotBlank()) {
                                viewModel.addHabit(nome, descricao)
                                showAddDialog = false
                            }
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



            // diaslog para editar hábito
            if (showEditDialog && habitToEdit != null) {
                var editNome by remember { mutableStateOf(habitToEdit!!.nome) }
                var editDescricao by remember { mutableStateOf(habitToEdit!!.descricao) }

                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = { Text("Editar Hábito") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = editNome,
                                onValueChange = { editNome = it },
                                label = { Text("Nome do Hábito") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = editDescricao,
                                onValueChange = { editDescricao = it },
                                label = { Text("Descrição") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            habitToEdit?.id?.let {
                                viewModel.updateHabit(it, editNome, editDescricao)
                            }
                            showEditDialog = false
                        }) {
                            Text("Guardar Alterações")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showEditDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            // dialog DE PARTILHA ADICIONADO
            if (showShareDialog && habitToShare != null) {
                AlertDialog(
                    onDismissRequest = { showShareDialog = false; emailShare = "" },
                    title = { Text("Partilhar Hábito: ${habitToShare!!.nome}") },
                    text = {
                        OutlinedTextField(
                            value = emailShare,
                            onValueChange = { emailShare = it },
                            label = { Text("E-mail do Utilizador") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (emailShare.isNotBlank() && habitToShare!!.id != null) {
                                viewModel.shareHabit(habitToShare!!.id!!, emailShare)
                                showShareDialog = false
                                emailShare = ""
                            }
                        }) {
                            Text("Partilhar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showShareDialog = false; emailShare = "" }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}