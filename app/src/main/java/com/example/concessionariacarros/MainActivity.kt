package com.example.concessionariacarros


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.concessionariacarros.model.Carro
import com.example.concessionariacarros.model.enum.TipoVeiculo

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuildLayout()
        }
    }
}


@Composable
fun BuildLayout() {
    var carroDisplay by remember {
        mutableStateOf(
            Carro(
                "chevrolet",
                TipoVeiculo.SEDAN,
                "ABC-1234",
                false
            )
        )
    }
    CarrosCard(carro = carroDisplay)
}

@Composable
fun CarrosCard(carro: Carro){
    var expandDetails by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                expandDetails = !expandDetails
            },
        elevation = 4.dp
    ){
        Row{
            Column(
                modifier = Modifier
                    .background(Color.Green)
                    .height(60.dp)
                    .weight(0.5f)

            ){
                Text(text = "")
            }
            Column(Modifier.weight(15f)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            expandDetails = !expandDetails
                        }
                ) {
                    Text(
                        text = "Modelo: " + carro.modelo,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(0.dp, 250.dp)
                    )
                    Row(
                        modifier =
                        Modifier.align(Alignment.CenterHorizontally)
                    ) {
                    }
                    AnimatedVisibility(
                        visible = expandDetails,
                        enter = fadeIn(initialAlpha = 0f) + expandVertically(),
                        exit = fadeOut(animationSpec = tween(durationMillis = 250)) + shrinkVertically()
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.description_text,
                                carro.placa,
                                carro.tipo.descricao,
                                carro.status
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun CarroList(carros: List<Carro>, onClick: (carro: Carro) -> Unit) {
    LazyColumn {
        items(carros) { carro ->
            CarrosCard(carro)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BuildLayout()
}