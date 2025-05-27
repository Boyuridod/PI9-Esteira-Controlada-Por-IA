package com.example.olhosdaesteira

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.olhosdaesteira.ui.theme.OlhosDaEsteiraTheme

class Projetos8 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OlhosDaEsteiraTheme {
                    TelaProjetos8()
            }
        }
    }
}

@Composable
fun TelaProjetos8() {
    Scaffold {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaProjetos8Preview() {
    OlhosDaEsteiraTheme {
        TelaProjetos8()
    }
}