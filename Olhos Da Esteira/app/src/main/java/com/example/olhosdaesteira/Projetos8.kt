package com.example.olhosdaesteira

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.olhosdaesteira.ui.theme.OlhosDaEsteiraTheme

class Projetos8Activity : ComponentActivity() {
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
    var ligado by remember { mutableStateOf(false) }
    var textoBotao = "Ligar lâmpada"
    var temperatura by remember { mutableStateOf(30) }

    Scaffold {
        innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    ligado = !ligado
                    if(ligado) {
                        textoBotao = "Desligar lâmpada"
                    }
                    else{
                        textoBotao = "Ligar lâmpada"
                    }
                },
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (ligado) 2.dp else 8.dp
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .width(200.dp)
                    .height(100.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(textoBotao)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Ligar o ventilador em: ${temperatura}°C",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Slider(
                    value = temperatura.toFloat(),
                    onValueChange = { temperatura = it.toInt() },
                    valueRange = 20f..40f,
                    steps = 15,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
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