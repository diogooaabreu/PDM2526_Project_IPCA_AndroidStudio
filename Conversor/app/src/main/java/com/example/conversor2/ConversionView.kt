package com.example.conversor2

// Importações Compose e Material 3
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.conversor2.ui.theme.Conversor2Theme
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionView(
    modifier: Modifier = Modifier
) {
    // Estados do UI
    var inputValue by remember { mutableStateOf("") } // valor introduzido pelo utilizador
    var selectedCategory by remember { mutableStateOf(0) } // índice da categoria escolhida
    var selectedFromUnit by remember { mutableStateOf(0) } // unidade origem
    var selectedToUnit by remember { mutableStateOf(1) }   // unidade destino
    var resultText by remember { mutableStateOf("Resultado") } // resultado mostrado no ecrã

    // Configuração das Unidades
    // Instância do motor de conversão
    val conversionBrain by remember { mutableStateOf(ConversionBrain()) }

    // Categorias disponíveis
    val categories = listOf("Comprimento", "Massa", "Temperatura")

    // Unidades por categoria
    val lengthUnits = listOf("m", "km", "cm", "mm", "in", "ft", "yd", "mi")
    val massUnits = listOf("kg", "g", "mg", "lb", "oz", "ton")
    val tempUnits = listOf("°C", "°F", "K")

    // Unidades ativas dependendo da categoria escolhida
    val units = when (selectedCategory) {
        0 -> lengthUnits
        1 -> massUnits
        2 -> tempUnits
        else -> lengthUnits
    }

    // Função executada ao clicar em "Converter"
    val onConvertPressed: () -> Unit = {
        val rawValue = inputValue.replace(',', '.') // Converte vírgula para ponto
        val value = rawValue.toDoubleOrNull() // Tenta converter para número

        if (value != null && inputValue.isNotEmpty()) {
            // Obtém as unidades e categoria
            val fromUnit = units[selectedFromUnit]
            val toUnit = units[selectedToUnit]
            val category = when (selectedCategory) { // categorias
                0 -> ConversionBrain.Category.LENGTH
                1 -> ConversionBrain.Category.MASS
                2 -> ConversionBrain.Category.TEMPERATURE
                else -> ConversionBrain.Category.LENGTH
            }

            // Chama a função de conversão
            val result = conversionBrain.convert(value, fromUnit, toUnit, category)

            // Formata o número final
            val decimalFormat = DecimalFormat("#,##0.#####")
            resultText = "${decimalFormat.format(result)} $toUnit"
        } else {
            resultText = "Insira um valor válido"
        }
    }

    // Troca as unidades entre origem e destino
    val onSwapPressed: () -> Unit = {
        val temp = selectedFromUnit
        selectedFromUnit = selectedToUnit
        selectedToUnit = temp
    }

    // Interface principal
    Column(
        modifier = modifier
            .fillMaxSize()  // */ ocupa o ecra
            .padding(16.dp),    // margem
        horizontalAlignment = Alignment.CenterHorizontally,// alinhamento horizontal
        verticalArrangement = Arrangement.spacedBy(16.dp)// espaçamento vertical
    ) {
        // Título principal
        Text(
            text = "Conversor de Unidades",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Campo de entrada numérica
        OutlinedTextField(
            value = inputValue, // valor do campo
            onValueChange = { inputValue = it }, // ação a executar ao alterar o valor
            label = { Text("Valor a converter") }, // label do campo
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // teclado numérico
            modifier = Modifier.fillMaxWidth(), // ocupa toda a largura
            singleLine = true, // permite apenas uma linha de texto
            placeholder = { Text("Ex: 100") } // texto de ajuda
        )

        // Escolha da categoria (comprimento, massa, temperatura)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Categoria:", style = MaterialTheme.typography.labelMedium)// label do campo
            Row(
                modifier = Modifier.fillMaxWidth(),// ocupa toda a largura
                horizontalArrangement = Arrangement.spacedBy(8.dp)// espaçamento horizontal
            ) {
                categories.forEachIndexed { index, category -> // para cada categoria
                    FilterChip(//filtro
                        selected = selectedCategory == index,// categoria selecionada
                        onClick = { // ação a executar ao clicar
                            // Atualiza o estado da categoria selecionada
                            selectedCategory = index
                            selectedFromUnit = 0
                            selectedToUnit = 1
                        },
                        label = { Text(category) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Escolha de unidades
        Row(
            modifier = Modifier.fillMaxWidth(),// ocupa toda a largura
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Unidade de origem
            Column(modifier = Modifier.weight(1f)) {
                var fromExpanded by remember { mutableStateOf(false) }

                Text("De:", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(// menu suspenso
                    expanded = fromExpanded,
                    onExpandedChange = { fromExpanded = !fromExpanded }
                ) {
                    OutlinedTextField(// campo de texto
                        value = units[selectedFromUnit],// valor do campo
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded)// ícone do menu suspenso
                        }
                    )
                    ExposedDropdownMenu(// menu suspenso
                        expanded = fromExpanded,
                        onDismissRequest = { fromExpanded = false }
                    ) {
                        units.forEachIndexed { index, unit ->// percorre cada unidade
                            DropdownMenuItem(// item do menu suspenso
                                text = { Text(unit) },// texto do item
                                onClick = {
                                    selectedFromUnit = index// atualiza o estado da unidade selecionada
                                    fromExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Unidade de destino
            Column(modifier = Modifier.weight(1f)) {
                var toExpanded by remember { mutableStateOf(false) }

                Text("Para:", style = MaterialTheme.typography.labelMedium)
                ExposedDropdownMenuBox(
                    expanded = toExpanded,
                    onExpandedChange = { toExpanded = !toExpanded }
                ) {
                    OutlinedTextField(
                        value = units[selectedToUnit],
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = toExpanded,
                        onDismissRequest = { toExpanded = false }
                    ) {
                        units.forEachIndexed { index, unit ->
                            DropdownMenuItem(
                                text = { Text(unit) },
                                onClick = {
                                    selectedToUnit = index
                                    toExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Botão de converter
        Button(
            onClick = onConvertPressed,// ação a executar ao clicar
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Converter", style = MaterialTheme.typography.titleMedium)// texto do botão
        }

        // Área do resultado
        Card(
            modifier = Modifier.fillMaxWidth(),//
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = resultText,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionViewPreview() {
    Conversor2Theme {
        ConversionView()
    }
}
