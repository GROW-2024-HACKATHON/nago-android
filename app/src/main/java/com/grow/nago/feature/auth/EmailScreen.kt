package com.grow.nago.feature.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.grow.nago.root.NavGroup
import com.grow.nago.ui.component.NagoButton
import com.grow.nago.ui.component.NagoTextField
import com.grow.nago.ui.theme.title1

@Composable
fun EmailScreen(
    viewModel: EmailViewModel = viewModel(),
    navBottomVisible: (Boolean) -> Unit,
    navController: NavController,
    phoneNum: String,
    wasName : String
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    var emailText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        navBottomVisible(false)
    }

    Column {
        Text(
            modifier = Modifier.padding(top = 50.dp, start = 18.dp, bottom = 20.dp),
            text = "이메일 주소를 입력해주세요",
            style = title1
        )

        NagoTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .focusRequester(focusRequester),
            value = emailText,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { emailText = it},
            hint = "이메일을 입력해주세요"
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Box(modifier = Modifier.weight(1f))
        NagoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            text = "다음",
            enabled = emailText.isNotEmpty(),
            onClick = {
                if (emailText.isNotEmpty()) {
                    viewModel.saveData(phoneNum, emailText, wasName)
                    navBottomVisible(true)
                    while (navController.popBackStack()) {

                    }
                    navController.navigate(NavGroup.LOG)
                }
                else{
                    Toast.makeText(context,"이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            },
            contentPadding = PaddingValues(vertical = 17.5.dp)
        )

    }
}
//@Preview
//@Composable
//private fun gffggf() {
//    NameScreen("원래 저겅ㅆ떤거")
//}
