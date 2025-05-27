package com.example.olhosdaesteira

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.olhosdaesteira.ui.theme.OlhosDaEsteiraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OlhosDaEsteiraTheme {
                TelaPrincipal()
            }
        }
    }
}

@Composable
fun TelaPrincipal() {
    val context = LocalContext.current

    Scaffold{
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
                    val intent = Intent(context, Projetos9Activity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Projetos 9")
            }
            Button(
                onClick = { /* ação do botão 2 */ },
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Projetos 8")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaPrincipalPreview() {
    OlhosDaEsteiraTheme {
        TelaPrincipal()
    }
}