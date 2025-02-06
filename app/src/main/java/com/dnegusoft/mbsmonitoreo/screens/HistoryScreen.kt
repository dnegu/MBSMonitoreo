package com.dnegusoft.mbsmonitoreo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnegusoft.mbsmonitoreo.db.entity.TimeMovEntity
import com.dnegusoft.mbsmonitoreo.model.HistoryAction
import com.dnegusoft.mbsmonitoreo.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreenRoot(
    onBack: () -> Boolean,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val movement by viewModel.movements.collectAsState()
    val loading by viewModel.loadingSending.collectAsState()

    viewModel.response.Observe { response ->
        response.id.let {
            if (it != null && it != 0L) {
                viewModel.updateState(it)
            }
        }
    }

    LaunchedEffect (Unit){
        viewModel.getMovements()
    }



    HistoryScreen(
        movement,
        onAction = { action ->
            when (action) {
                HistoryAction.OnBack -> {
                    onBack()
                }
                HistoryAction.OnSendPending -> {
                    viewModel.sendMovementsPending()
                }
                else -> Unit
            }
        }
    )

    if (loading)
        IndeterminateLinearLoader(
            text = "Enviando marcas pendientes..."
        )
}

@Composable
fun HistoryScreen(
    movements: List<TimeMovEntity>,
    onAction: (HistoryAction) -> Unit
) {
    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val todayFormatted = today.format(dateFormatter)

    val todayMovements = movements.filter { it.fecha == todayFormatted }

    Column {
        WCTopBarWithMenuIcon(
            title = "Historial",
            onMenuClick = {
                onAction(HistoryAction.OnBack)
            }
        )

        Text(
            text = "Movimientos de hoy: $todayFormatted",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if(todayMovements.any { it.status == "P" })
            Button(
                onClick = {
                    onAction(HistoryAction.OnSendPending)
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    "Enviar marcas pendientes"
                )
            }

        LazyColumn(
            modifier = Modifier.padding(16.dp,0.dp,16.dp,16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(todayMovements) { movement ->
                MovementItem(movement = movement)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovementItem(movement: TimeMovEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Actividad:", fontWeight = FontWeight.Bold)
                    Text(text = movement.actividad)
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Fecha:", fontWeight = FontWeight.Bold)
                    Text(text = movement.fecha)
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Hora:", fontWeight = FontWeight.Bold)
                    Text(text = movement.hora)
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Tipo:", fontWeight = FontWeight.Bold)
                    Text(text = movement.tipo)
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Máquina:", fontWeight = FontWeight.Bold)
                    Text(text = movement.maquina)
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Persona:", fontWeight = FontWeight.Bold)
                    Text(text = movement.personId)
                }
            }
            StatusLabel(
                status = movement.status,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun StatusLabel(status: String, modifier: Modifier = Modifier) {
    val (text, color) = when (status) {
        "P" -> "Pendiente" to Color.Red
        "F" -> "Enviado" to Color.Green
        else -> "Desconocido" to Color.Gray
    }

    Box(
        modifier = modifier
            .background(color)
            .padding(8.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun WCTopBarWithMenuIcon(
    title: String,
    onMenuClick: () -> Unit,
    background: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = contentColorFor(backgroundColor = background),
    icon: ImageVector = Icons.Default.ArrowBack
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(background)
            .padding(horizontal = 6.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(imageVector = icon, contentDescription = null, tint = textColor)
        }
        Text(title, style = MaterialTheme.typography.titleLarge, color = textColor)
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    MaterialTheme(
        typography = MaterialTheme.typography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HistoryScreen(
                listOf(
                    TimeMovEntity(
                        id = 1,
                        actividad = "actividad",
                        fecha = "2025-02-06",
                        hora = "hora",
                        tipo = "tipo",
                        maquina = "maquina",
                        personId = "personId",
                        status = "P"
                    ),
                    TimeMovEntity(
                        id = 1,
                        actividad = "actividad",
                        fecha = "2025-02-06",
                        hora = "hora",
                        tipo = "tipo",
                        maquina = "maquina",
                        personId = "personId",
                        status = "F"
                    ),
                    TimeMovEntity(
                        id = 1,
                        actividad = "actividad",
                        fecha = "2025-02-06",
                        hora = "hora",
                        tipo = "tipo",
                        maquina = "maquina",
                        personId = "personId",
                        status = "F"
                    ),
                    TimeMovEntity(
                        id = 1,
                        actividad = "actividad",
                        fecha = "2025-02-06",
                        hora = "hora",
                        tipo = "tipo",
                        maquina = "maquina",
                        personId = "personId",
                        status = "F"
                    ),
                    TimeMovEntity(
                        id = 1,
                        actividad = "actividad",
                        fecha = "2025-02-06",
                        hora = "hora",
                        tipo = "tipo",
                        maquina = "maquina",
                        personId = "personId",
                        status = "F"
                    ),
                    TimeMovEntity(
                        id = 1,
                        actividad = "actividad",
                        fecha = "2025-02-06",
                        hora = "hora",
                        tipo = "tipo",
                        maquina = "maquina",
                        personId = "personId",
                        status = "F"
                    ),
                    TimeMovEntity(
                        id = 1,
                        actividad = "actividad",
                        fecha = "2025-02-06",
                        hora = "hora",
                        tipo = "tipo",
                        maquina = "maquina",
                        personId = "personId",
                        status = "F"
                    )
                ),
                onAction = {}
            )
        }
    }
}