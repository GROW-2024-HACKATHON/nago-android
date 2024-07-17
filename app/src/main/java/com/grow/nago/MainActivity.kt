package com.grow.nago

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grow.nago.root.NavGroup
import com.grow.nago.ui.theme.NagoTheme
import com.grow.nago.ui.theme.White

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            NagoTheme {
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

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NagoTheme {
        Greeting("Android")
    }
}