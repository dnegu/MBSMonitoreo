package com.dnegusoft.mbsmonitoreo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.dnegusoft.mbsmonitoreo.constants.Route
import com.dnegusoft.mbsmonitoreo.screens.HistoryScreenRoot
import com.dnegusoft.mbsmonitoreo.screens.LoginScreenRoot
import com.dnegusoft.mbsmonitoreo.screens.MainScreenRoot
import com.dnegusoft.mbsmonitoreo.ui.theme.MBSMonitoreoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MBSMonitoreoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    paddingValues
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Route.AuthGraph
                    ) {
                        navigation<Route.AuthGraph>(
                            startDestination = Route.Login
                        ){
                            composable<Route.Login> {
                                LoginScreenRoot(
                                    onLoginSuccess = {
                                        navController.navigate(Route.HomeGraph) {
                                            popUpTo(Route.AuthGraph) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        navigation<Route.HomeGraph>(
                            startDestination = Route.Home
                        ){
                            composable<Route.Home> {
                                MainScreenRoot(
                                    onBack = {navController.popBackStack()},
                                    onHistory = {
                                        navController.navigate(Route.History)
                                    }
                                )
                            }

                            composable<Route.History> {
                                HistoryScreenRoot(
                                    onBack = {navController.popBackStack()},
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
