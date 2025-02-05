package com.dnegusoft.mbsmonitoreo.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnegusoft.mbsmonitoreo.model.HomeAction
import com.dnegusoft.mbsmonitoreo.model.HomeState
import com.dnegusoft.mbsmonitoreo.viewmodel.ActivityState
import com.dnegusoft.mbsmonitoreo.viewmodel.HomeViewModel
import com.dnegusoft.mbsmonitoreo.viewmodel.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenRoot(
    onBack: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startClock()
    }

    MainScreen(
        uiState = uiState,
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                HomeAction.OnBack -> {
                    onBack()
                }
                else -> Unit
            }
            viewModel.onAction(action)
        },
        toggleDropdown = viewModel::toggleDropdown,
        toggleActivityDropdown = viewModel::toggleDropdownActivity,
        machines = viewModel.machines,
        selectMachine = viewModel::selectMachine,
        selectActivity = viewModel::selectActivity,
        confirmMachineSelection = viewModel::confirmMachineSelection,
        confirmActivitySelection = viewModel::confirmActivitySelection,
        toggleActivity = viewModel::toggleActivity,
        activities = viewModel.activities
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    uiState: UiState,
    toggleDropdown: () -> Unit,
    toggleActivityDropdown: () -> Unit,
    machines: List<String>,
    selectMachine: (String) -> Unit,
    selectActivity: (String) -> Unit,
    confirmMachineSelection: () -> Unit,
    confirmActivitySelection: (Boolean) -> Unit,
    toggleActivity: () -> Unit,
    activities: List<String>
) {
    var showDialog by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }
    var showSaveMarcation by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(uiState.currentTime, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Select de máquinas
        ExposedDropdownMenuBox(
            expanded = uiState.isDropdownExpanded,
            onExpandedChange = { toggleDropdown() }
        ) {
            OutlinedTextField(
                value = uiState.selectedMachine ?: "Seleccione una máquina",
                onValueChange = {},
                readOnly = true,
                enabled = !uiState.isMachineSelected,
                modifier = Modifier.menuAnchor()
            )

            DropdownMenu(
                expanded = uiState.isDropdownExpanded,
                onDismissRequest = { toggleDropdown() }
            ) {
                machines.forEach { machine ->
                    DropdownMenuItem(
                        text = { Text(machine) },
                        onClick = { selectMachine(machine) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón seleccionar/cambiar máquina
        if (!uiState.isMachineSelected)
            Button(
                onClick = { showDialog = true },
                enabled = uiState.selectedMachine != null
            ) {
                Text(if (uiState.isMachineSelected) "Cambiar máquina" else "Escoger máquina")
            }
        else{

            Spacer(modifier = Modifier.height(24.dp))
            // Select de acividad
            ExposedDropdownMenuBox(
                expanded = uiState.isActivityDropdownExpanded,
                onExpandedChange = { toggleActivityDropdown() }
            ) {
                OutlinedTextField(
                    value = uiState.selectedActivity ?: "Seleccione una actividad",
                    onValueChange = {},
                    readOnly = true,
                    enabled = !uiState.isActivitySelected,
                    modifier = Modifier.menuAnchor()
                )

                DropdownMenu(
                    expanded = uiState.isActivityDropdownExpanded,
                    onDismissRequest = { toggleActivityDropdown() }
                ) {
                    activities.forEach { activity ->
                        DropdownMenuItem(
                            text = { Text(activity) },
                            onClick = { selectActivity(activity) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón seleccionar/cambiar máquina
            Button(
                onClick = { showActivityDialog = true }
            ) {
                Text(if (uiState.isActivitySelected) "Cambiar actividad" else "Escoger actividad")
            }
        }



        Spacer(modifier = Modifier.height(32.dp))

        // Botón de inicio/fin de actividad
        if (uiState.isMachineSelected && uiState.isActivitySelected)
            uiState.activityState.let {
                Button(
                    onClick = { showSaveMarcation = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (it == ActivityState.INICIO) Color.Green else Color.Red
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth().height(260.dp)
                ) {
                    Text(
                        if (it == ActivityState.INICIO) "Inicio de actividad" else "Fin de actividad",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(fontSize = 40.sp, color = if (it == ActivityState.INICIO) Color.Black else Color.White),
                        textAlign = TextAlign.Center
                    )
                }
            }

        // Dialogo de confirmación de máquina
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro de seleccionar la máquina: ${uiState.selectedMachine}?") },
                confirmButton = {
                    Button(onClick = {
                        if(uiState.isMachineSelected)
                            toggleDropdown()
                        else
                            confirmMachineSelection()
                        showDialog = false
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }

        if (showActivityDialog) {
            AlertDialog(
                onDismissRequest = { showActivityDialog = false },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro de seleccionar la actividad: ${uiState.selectedActivity}?") },
                confirmButton = {
                    Button(onClick = {
                        if(uiState.activityState.name == "FIN")
                            showError = true
                        else
                            confirmActivitySelection(!uiState.isActivitySelected)
                        showActivityDialog = false
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(onClick = { showActivityDialog = false }) {
                        Text("No")
                    }
                }
            )
        }

        // Dialogo de confirmación de inicio/fin de actividad
        if (showSaveMarcation) {
            AlertDialog(
                onDismissRequest = { showSaveMarcation = false },
                title = { Text("Confirmación") },
                text = { Text("¿Está seguro de ${if (uiState.activityState == ActivityState.INICIO) "iniciar" else "finalizar"} \nla actividad : ${uiState.selectedActivity} \nen la máquina: ${uiState.selectedMachine}?") },
                confirmButton = {
                    Button(onClick = {
                        toggleActivity()
                        showSaveMarcation = false
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    Button(onClick = { showSaveMarcation = false }) {
                        Text("No")
                    }
                }
            )
        }

        if(showError){
            Toast.makeText(LocalContext.current, "No se puede finalizar una actividad en estado FIN", Toast.LENGTH_SHORT).show()
            showError = false
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MaterialTheme(
        typography = MaterialTheme.typography
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen(
                state = HomeState(),
                onAction = {},
                uiState = UiState(
                    currentTime = "11:58:00",
                    isMachineSelected = true,
                    isActivitySelected = true
                ),
                toggleDropdown = {},
                toggleActivityDropdown = {},
                machines = emptyList(),
                selectMachine = {},
                selectActivity = {},
                confirmMachineSelection = {},
                confirmActivitySelection = {},
                toggleActivity = {},
                activities = emptyList()
            )
        }
    }
}