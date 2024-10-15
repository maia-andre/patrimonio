package com.almoxarifado.psjc2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.almoxarifado.psjc2.ui.theme.Psjc2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instância do DatabaseHelper
        val dbHelper = DatabaseHelper(this)

        setContent {
            Psjc2Theme {
                MainScreen(dbHelper = dbHelper)
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, dbHelper: DatabaseHelper?) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val showInfo = remember { mutableStateOf(false) }
    val patrimonioInfo = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 1. Imagem
        Image(
            painter = painterResource(id = R.drawable.logo_psjc), // Altere para sua imagem
            contentDescription = "Imagem de Patrimônio",
            modifier = Modifier.size(128.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Texto
        Text(
            text = "Patrimônio Físico",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Caixa de Texto (TextField)
        OutlinedTextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            label = { Text(text = "Digite o código do patrimônio") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Botão para buscar o código
        Button(
            onClick = {
                if (dbHelper != null) {
                    val cursor = dbHelper.getPatrimonioByPlaca(textState.value.text)
                    if (cursor != null && cursor.moveToFirst()) {
                        val descricao = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRICAO))
                        val uo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UO))
                        patrimonioInfo.value = "Descrição: $descricao\nUO: $uo"
                        showInfo.value = true
                    } else {
                        patrimonioInfo.value = "Código não encontrado"
                        showInfo.value = false
                    }
                } else {
                    patrimonioInfo.value = "Sem conexão com o banco"
                    showInfo.value = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar Código")
        }

        // 5. Caixa com informações do patrimônio (aparecerá apenas se showInfo for true)
        if (showInfo.value) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = patrimonioInfo.value,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Psjc2Theme {
        MainScreen(dbHelper = null) // Passa null para o dbHelper no modo de preview
    }
}
