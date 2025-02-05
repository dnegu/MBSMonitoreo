package com.dnegusoft.mbsmonitoreo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dnegusoft.mbsmonitoreo.model.HomeAction
import com.dnegusoft.mbsmonitoreo.model.HomeState
import com.dnegusoft.mbsmonitoreo.viewmodel.ApiViewModel
import com.dnegusoft.mbsmonitoreo.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenRoot(
    onBack: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    MainScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                HomeAction.OnBack -> {
                    onBack()
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun MainScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    var fact by remember { mutableStateOf("") }
    val apiViewModel = koinViewModel<ApiViewModel>()

    LaunchedEffect(Unit) {
        apiViewModel.fetchData()
    }

    apiViewModel.data.Observe {
        fact = it.fact
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hola David!", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(fact)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onAction(HomeAction.OnBack) }) {
            Text("Salir")
        }
    }
}