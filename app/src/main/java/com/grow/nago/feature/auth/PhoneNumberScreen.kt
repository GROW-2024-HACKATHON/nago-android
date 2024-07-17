package com.grow.nago.feature.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grow.nago.root.NavGroup
import com.grow.nago.ui.component.NagoButton
import com.grow.nago.ui.component.NagoTextField
import com.grow.nago.ui.theme.Gray400
import com.grow.nago.ui.theme.title1


@Composable
fun PhoneNumberScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }


    var phoneNumber by remember {
        mutableStateOf("")
    }
    Column {
        Text(
            modifier = Modifier.padding(top =50.dp, start = 18.dp, bottom = 20.dp),
            text = "전화번호를 입력해주세요",
            style = title1)

        NagoTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .focusRequester(focusRequester),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            hint = "전화번호를 입력해주세요",
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Box(modifier = Modifier.weight(1f))
        NagoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
            ,
            text = "다음",
            enabled = phoneNumber.isNotEmpty(),
            onClick = {
                val isInt = phoneNumber.isDigitsOnly()
                if (!isInt) {
                    Toast.makeText(context,"숫자만 입력해주세요.",Toast.LENGTH_SHORT).show()
                }
                else if(phoneNumber.length != 11){
                    Toast.makeText(context,"전화번호 양식을 지켜주세요.",Toast.LENGTH_SHORT).show()
                }
                else{
                    navController.navigate("name/${phoneNumber}")
                }
            },
            contentPadding = PaddingValues(vertical = 17.5.dp)
        )

    }

}


//@Preview
//@Composable
//private fun gffggf() {
//    PhoneNumberScreen()
//}
