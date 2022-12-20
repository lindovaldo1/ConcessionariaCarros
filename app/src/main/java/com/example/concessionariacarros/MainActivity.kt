package com.example.concessionariacarros


import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.concessionariacarros.model.*
import com.example.concessionariacarros.model.enum.TipoVeiculo
import java.util.stream.Stream


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
                "",
                TipoVeiculo.SEDAN,
                0.0,
                true,

            )
        )
    }
    CarrosCard(carro = carroDisplay)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CarrosCard(carro: Carro){
    var expandDetails by remember { mutableStateOf(false) }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(("")) }
    var selectedOptionText by rememberSaveable() { mutableStateOf(TipoVeiculo.SEDAN.descricao) }
    val numberRegex = remember { "[\\d]*[,]?[\\d]*".toRegex() }
    var options = enumValues<TipoVeiculo>().toList()
    var expanded by remember { mutableStateOf(false) }
    val mContext = LocalContext.current





    Column() {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 4.dp
        ) {
            Column() {
                OutlinedTextField(modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                    value = model,
                    label = { Text("Modelo") },
                    onValueChange = { newValue ->
                        model= newValue
                    }

                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    value = price,
                    label = { Text("Preço") },
                    onValueChange = {
                        if (numberRegex.matches(it)) {
                                price = getValidatedNumber(it)
                        }


                    }
                )


                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },

                ) {
                    TextField(
                        readOnly = true,
                        value = selectedOptionText,
                        onValueChange = { },
                        label = { Text("Tipo") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp)
                            .border(
                                1.dp,
                                colorResource(R.color.cinza_borda),
                                shape = RoundedCornerShape(5.dp, 5.dp, 0.dp, 0.dp)
                            ),
                        colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = Color.White),


                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOptionText = selectionOption.descricao
                                    expanded = false
                                }
                            ) {
                                Text(text = selectionOption.descricao)
                            }
                        }
                    }
                }




                Column(modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            if (getValidatedVehicle(model, price, selectedOptionText, mContext) != null) {
                                CarroList.add(
                                    registerVehicle(
                                        model,
                                        price,
                                        year,
                                        selectedName,
                                        mContext
                                    )!!
                                );
                            }
                        },

                        ) {

                        Text("Adicionar")

                    }
                }

            }

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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

}


fun getValidatedNumber(text: String): String {

    return if(text.contains(',')) {
        val beforeDecimal = text.substringBefore(',')
        val afterDecimal = text.substringAfter(',')
        beforeDecimal + "," + afterDecimal.take(2)
    } else {
        text + ",00"
    }
}

fun getValidatedVehicle(
    modelo: TextFieldValue,
    preco: String,
    tipoString: String,
    mContext: Context
): Carro? {
    var tipo : TipoVeiculo = TipoVeiculo.SEDAN
    var tipos = enumValues<TipoVeiculo>().toList()
    tipos.forEach {
       if (tipoString.equals(it.descricao)){
           tipo = it
       }
    }
    if (modelo.text == "") {
        Toast.makeText(mContext, "Modelo inválido", Toast.LENGTH_LONG).show()
        return null;
    } else if (preco == "") {
        Toast.makeText(mContext, "Preço inválido", Toast.LENGTH_LONG).show()
        return null;

    } else if (modelo.text != "" && preco != "") {
        var carro = Carro(modelo.text, tipo, preco.toDouble(), false);
        return carro;
    }
    return null;
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


