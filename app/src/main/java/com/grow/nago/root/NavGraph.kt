package com.grow.nago.root

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grow.nago.Greeting
import com.grow.nago.R
import com.grow.nago.feature.auth.EmailScreen
import com.grow.nago.feature.auth.NameScreen
import com.grow.nago.feature.auth.PhoneNumberScreen
import com.grow.nago.feature.camera.CameraScreen
import com.grow.nago.feature.home.HomeScreen
import com.grow.nago.feature.log.LogScreen
import com.grow.nago.feature.logdetail.LogDetailScreen
import com.grow.nago.feature.onboard.OnBoardScreen
import com.grow.nago.feature.setting.SettingScreen
import com.grow.nago.local.sharedpreferences.NagoSharedPreferences
import com.grow.nago.ui.animation.bounceClick
import com.grow.nago.ui.component.DropShadowType
import com.grow.nago.ui.component.dropShadow
import com.grow.nago.ui.theme.Gray400
import com.grow.nago.ui.theme.Gray500
import com.grow.nago.ui.theme.Orange300
import com.grow.nago.ui.theme.White
import com.grow.nago.ui.theme.caption2
import java.util.jar.Attributes.Name

@Composable
fun NavGraph(){

    val navHostController = rememberNavController()
    val backstackEntry by navHostController.currentBackStackEntryAsState()
    val selectRoute = backstackEntry?.destination?.route
    var isShowNavBar by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var isLogin: Boolean? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = true) {
        isLogin = NagoSharedPreferences.getNagoSharedPreferences().myName.isNotEmpty()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = White
    ) {
        Scaffold(
            bottomBar = {
                if (isShowNavBar) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .dropShadow(DropShadowType.EvBlack3)
                            .background(White)
                    ) {
                        NavCard(
                            modifier = Modifier
                                .weight(1f)
                                .bounceClick(
                                    onClick = {
                                        navHostController.navigate(NavGroup.LOG) {
                                            popUpTo(navHostController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                ),
                            resId = R.drawable.ic_normal_book,
                            text = "기록",
                            isSelected = selectRoute == NavGroup.LOG
                        )

                        Image(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically)
                                .bounceClick(
                                    onClick = {
                                        navHostController.navigate(NavGroup.CAMERA)
                                    }
                                ),
                            painter = painterResource(id = R.drawable.ic_normal_plus),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Gray400)
                        )

                        NavCard(
                            modifier = Modifier
                                .weight(1f)
                                .bounceClick(
                                    onClick = {
                                        navHostController.navigate(NavGroup.SETTING)
                                    }
                                ),
                            resId = R.drawable.ic_normal_setting,
                            text = "설정",
                            isSelected = selectRoute == "setting"
                        )

                    }
                }
            }
        ) {
            isLogin?.let { isLogin ->
                NavHost(
                    modifier = Modifier.padding(it),
                    navController = navHostController,
                    startDestination = if (isLogin) NavGroup.LOG else NavGroup.ONBOARD
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
                        navHostController.navigate(NavGroup.PHONE)
                    }

                    composable(
                        route = NavGroup.HOME
                    ) {
                        HomeScreen()
                    }

                    composable(
                        route = NavGroup.LOG
                    ) {
                        LogScreen(
                            navController = navHostController
                        )
                    }
                    composable(
                        route = NavGroup.PHONE
                    ){
                        PhoneNumberScreen(
                            navController = navHostController,
                            navBottomVisible = {
                                isShowNavBar = it
                            }
                        )
                    }
                    composable(
                        route = NavGroup.NAME,
                        arguments = listOf(
                            navArgument("phone") { NavType.StringType }
                        )){
                        val phoneNum =  it.arguments?.getString("phone")?: ""
                        NameScreen(
                            navController = navHostController,
                            phoneNum = phoneNum,
                            navBottomVisible = {
                                isShowNavBar = it
                            }
                        )
                    }

                    composable(
                        route = NavGroup.EMAIL,
                        arguments =listOf(
                            navArgument("phone") { NavType.StringType},
                            navArgument("name") { NavType.StringType}
                        )
                    ){
                        val phoneNum =  it.arguments?.getString("phone")?: ""
                        val name = it.arguments?.getString("name")?: ""
                        EmailScreen(
                            navController = navHostController,
                            phoneNum = phoneNum,
                            wasName = name,
                            navBottomVisible = {
                                isShowNavBar = it
                            }
                        )
                    }
                    composable(NavGroup.CAMERA) {
                        CameraScreen(
                            navController = navHostController,
                            navVisibleChange = {
                                isShowNavBar = it
                            }
                        )
                    }

                    composable(NavGroup.ONBOARD) {
                        OnBoardScreen(
                            navController = navHostController,
                            navVisibleChange = {
                                isShowNavBar = it
                            }
                        )
                    }

                    composable(
                        route = NavGroup.LOG_DETAIL,
                        arguments = listOf(
                            navArgument("id") { NavType.StringType }
                        )
                    ) {
                        val id = it.arguments?.getString("id")?.toInt()?: 0
                        LogDetailScreen(
                            id = id,
                            navController = navHostController,
                            navBottomVisible = {
                                isShowNavBar = it
                            }
                        )
                    }

                    composable(
                        route = NavGroup.SETTING,
                    ) {
                        SettingScreen(navController = navHostController)
                    }

                }
            }
        }
    }
}

@Composable
private fun NavCard(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
    text: String,
    isSelected: Boolean,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(if (isSelected) Orange300 else Gray400)
        )
        Text(
            text = text,
            color = if (isSelected) Orange300 else Gray500,
            style = caption2
        )
    }
}