package com.example.concessionariacarros


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.example.concessionariacarros.model.Carro
import com.example.concessionariacarros.model.enum.TipoVeiculo
import com.example.concessionariacarros.ui.theme.InserirCarrosTheme

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
    ) {

        Column(
            modifier = Modifier
                .background(Color.Green)
                .fillMaxWidth()
                .padding(16.dp))
        {

            Row{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            expandDetails = !expandDetails
                        }
                ) {
                    Text(
                        text = "Model: " + carro.modelo,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .widthIn(0.dp, 250.dp)
                            .clickable {
                                expandDetails = !expandDetails
                            }
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
                                carro.tipo.descricao,
                                carro.placa, carro.status
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

        }

//        Row{
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .clickable {
//                        expandDetails = !expandDetails
//                    }
//            ) {
//                Text(
//                    text = "Model: " + carro.modelo,
//                    textAlign = TextAlign.Center,
//                    fontSize = 24.sp,
//                    overflow = TextOverflow.Ellipsis,
//                    maxLines = 1,
//                    modifier = Modifier
//                        .widthIn(0.dp, 250.dp)
//                        .clickable {
//                            expandDetails = !expandDetails
//                        }
//                )
//                Row(
//                    modifier =
//                    Modifier.align(Alignment.CenterHorizontally)
//                ) {
//                }
//                AnimatedVisibility(
//                    visible = expandDetails,
//                    enter = fadeIn(initialAlpha = 0f) + expandVertically(),
//                    exit = fadeOut(animationSpec = tween(durationMillis = 250)) + shrinkVertically()
//                ) {
//                    Text(
//                        text = stringResource(
//                            id = R.string.description_text,
//                            carro.tipo.descricao,
//                            carro.placa, carro.status
//                        ),
//                        modifier = Modifier.padding(8.dp)
//                    )
//                }
//            }
//        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BuildLayout()
}