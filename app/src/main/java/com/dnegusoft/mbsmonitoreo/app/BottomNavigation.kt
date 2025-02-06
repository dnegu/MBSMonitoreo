package com.dnegusoft.mbsmonitoreo.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dnegusoft.mbsmonitoreo.constants.Route


sealed class BottomBarScreen(val route: Route, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen(Route.Home, "Home", Icons.Filled.Home)
    object History : BottomBarScreen(Route.History, "History",
        Icons.Filled.List)
}

@Composable
fun BottomNavigationBar(navController: NavHostController, items: List<BottomBarScreen>) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            if (currentRoute != null) {
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = true,
                    onClick = {
                        navController.navigate(currentRoute) {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}