package com.grow.nago.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grow.nago.Greeting
import com.grow.nago.feature.home.HomeScreen
import com.grow.nago.ui.theme.White

@Composable
fun NavGraph(){

    val navHostController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = White
    ) {
        NavHost(
            navController = navHostController,
            startDestination = NavGroup.LOGIN
        ) {
            composable(NavGroup.LOGIN) {
                Greeting(name = "test")
                LaunchedEffect(key1 = true) {
                    navHostController.navigate("test/qwewqqwe")
                }
            }
            composable(
                route = "test/{qwer}",
                arguments = listOf(
                    navArgument("qwer") { NavType.StringType }
                )
            ) {
                val qwer =  it.arguments?.getString("qwer")?: ""
                Greeting(name = qwer)
            }

            composable(
                route = NavGroup.HOME
            ) {
                HomeScreen()
            }

        }
    }
}