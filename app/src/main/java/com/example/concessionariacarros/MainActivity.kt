package com.example.concessionariacarros


import MainScreenView
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.concessionariacarros.model.enum.VehicleType
import androidx.compose.foundation.*
import androidx.compose.ui.text.style.TextDecoration


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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.onPrimary
    ){
        MainScreenView()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun homeScreen(vehiclesList: SnapshotStateList<Vehicle>){

    var vehicle by remember {
        mutableStateOf(
            Vehicle(
                "",
                0.0,
                VehicleType.SEDAN,
                false,
            )
        )
    }
    var model by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(("")) }
    var type by rememberSaveable() { mutableStateOf(VehicleType.SEDAN.description) }
    val priceRegex = remember { "[\\d]*[.]?[\\d]*".toRegex() }
    var vehicleTypes = enumValues<VehicleType>().toList()
    var expanded by remember { mutableStateOf(false) }
    val mContext = LocalContext.current

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
                    if (priceRegex.matches(it)) {
                        price = formatVehiclePrice(it)
                    }
                }
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                ) {
                OutlinedTextField(
                    readOnly = true,
                    value = type,
                    onValueChange = { },
                    label = { Text("Tipo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    colors = ExposedDropdownMenuDefaults.textFieldColors(backgroundColor = Color.White),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    vehicleTypes.forEach { selectedType ->
                        DropdownMenuItem(
                            onClick = {
                                type = selectedType.description
                                expanded = false
                            }
                        ) {
                            Text(text = selectedType.description)
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        if (addVehicle(model, price, type, mContext) != null) {
                            vehiclesList.add(
                                addVehicle(
                                    model,
                                    price,
                                    type,
                                    mContext
                                )!!
                            );
                            model = TextFieldValue("")
                            price = ""
                            type = VehicleType.SEDAN.description
                        }
                    },
                    ) {

                    Text("Adicionar")
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                listVehicles(vehiclesList) { vehicleItem ->
                    vehicle = vehicleItem
                }
            }
        }
    }
}

@Composable
fun statisticsScreen(vehiclesList: SnapshotStateList<Vehicle>) {
    var totalCars = 0;
    var soldCars  = 0;
    var availableCars  = 0;

    vehiclesList.forEach { vehicle ->
        if(vehicle.sold){
            soldCars ++;
        }else{
            availableCars ++;
        }
        totalCars ++;
    }
    
    Column() {
        Card(modifier = Modifier.fillMaxWidth().padding(all = 10.dp), elevation = 4.dp) {
            Column() {
                Text(text = "Total de carros no sistema: $totalCars", modifier = Modifier.padding(all = 6.dp), fontSize = 20.sp)

                Text(text = "Carros disponíveis: $availableCars", modifier = Modifier.padding(all = 6.dp), fontSize = 18.sp)

                Text(text = "Carros vendidos: $soldCars", modifier = Modifier.padding(all = 6.dp), fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VehicleInfo(vehicle: Vehicle){

    var expandDetails by remember { mutableStateOf(false) }

    Column() {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .combinedClickable(
                    onClick = {
                        expandDetails = !expandDetails
                    },
                    onLongClick = {
                        vehicle.sold = !vehicle.sold
                    }
                ),
            elevation = 4.dp
        ){
            Row(){
                Column(
                    modifier = Modifier
                        .background(
                            if (vehicle.sold) {
                                Color.Red
                            } else {
                                Color.Green
                            }
                        )
                        .height(64.dp)
                        .weight(0.5f)

                ){
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
                            text = vehicle.model,
                            style =   if (vehicle.sold) {
                                TextStyle(textDecoration = TextDecoration.LineThrough)
                            } else {
                                TextStyle(textDecoration = TextDecoration.None)
                            },
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
                            exit = fadeOut(animationSpec = tween(durationMillis = 250)) + shrinkVertically(),

                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.description_text,
                                    vehicle.type.description,vehicle.price,

                                    if (!vehicle.sold) {
                                        "Veículo disponível"
                                    } else {
                                        "Veículo vendido"
                                    }
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

@Composable
fun listVehicles(vehicles: List<Vehicle>, onClick: (vehicle: Vehicle) -> Unit) {
    LazyColumn {
        items(vehicles) { vehicle ->
            VehicleInfo(vehicle)
        }
    }
}

fun formatVehiclePrice(text: String): String {

    return if(text.contains('.')) {
        val beforeDecimal = text.substringBefore('.')
        val afterDecimal = text.substringAfter('.')
        beforeDecimal + "." + afterDecimal.take(2)
    } else {
        "$text.00"
    }
}

fun addVehicle(
    model: TextFieldValue,
    price: String,
    typeString: String,
    mContext: Context
): Vehicle? {
    var type : VehicleType = VehicleType.SEDAN
    var types = enumValues<VehicleType>().toList()
    types.forEach {
       if (typeString.equals(it.description)){
           type = it
       }
    }
    if (model.text == "") {
        Toast.makeText(mContext, "O modelo não pode ser vazio", Toast.LENGTH_LONG).show()
        return null;
    } else if (price == "") {
        Toast.makeText(mContext, "O preço não pode ser vazio", Toast.LENGTH_LONG).show()
        return null;

    } else if (model.text != "" && price != "") {
        var vehicle = Vehicle(model.text, price.toDouble(), type, false);
        return vehicle;
    }
    return null;
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BuildLayout()
}


