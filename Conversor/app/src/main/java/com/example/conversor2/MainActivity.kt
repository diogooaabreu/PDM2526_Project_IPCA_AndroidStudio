package com.example.conversor2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.conversor2.ui.theme.Conversor2Theme

// A MainActivity é o ponto de entrada principal da aplicação.
// É onde o Jetpack Compose é inicializado e o conteúdo do ecrã é definido.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define o conteúdo da aplicação usando Jetpack Compose.
        setContent {

            // Usa o tema definido no projeto (em ui/theme/Theme.kt)
            Conversor2Theme {

                // Surface é o contêiner base onde o conteúdo é desenhado.
                // A cor de fundo é herdada do tema atual.
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aqui é onde chamamos a função que constrói o ecrã do conversor.
                    ConversionView()
                }
            }
        }
    }
}
