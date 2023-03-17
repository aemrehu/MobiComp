package com.codemave.mobicomphw1.ui.login

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import com.codemave.mobicomphw1.Graph
import com.codemave.mobicomphw1.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

@Composable
fun LoginScreen(
    context: Context,
    navController: NavController,
    getGoogleLoginAuth: GoogleSignInClient
) {
    // Google login:
    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            println("Started activity")
//            if (result.resultCode == Activity.RESULT_OK) {
//                println("Activity RESULT_OK")
                val intent = result.data
//                println("${intent?.extras}")
                if (result.data != null) {
                    println("result.data != null")
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    handleSignInResult(task, navController)

                }
//            } else {
//                println("Result code NOT OK")
//            }
        }

    // Google login ends.

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "Welcome!",
            fontSize = 8.em
        )
        Spacer(modifier = Modifier.height(20.dp))
        Icon(
            painter = rememberVectorPainter(Icons.Filled.AccessAlarm),
            contentDescription = "login_image",
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
        )
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.6F)
                .height(50.dp),
            onClick = {
                println("Clicked LOGIN")
                //navController.navigate("home")
                startForResult.launch(getGoogleLoginAuth.signInIntent)
            },
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Login with Google")
        }

//        val username = remember { mutableStateOf("") }
//        val password = remember { mutableStateOf("") }
//
//        Icon(
//            painter = rememberVectorPainter(Icons.Filled.Person),
//            contentDescription = "login_image",
//            modifier = Modifier.fillMaxWidth().size(150.dp)
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        OutlinedTextField(
//            modifier = Modifier.fillMaxWidth(0.9F),
//            value = username.value,
//            onValueChange = { text -> username.value = text },
//            label = {Text(text = "Username") },
//            shape = RoundedCornerShape(corner = CornerSize(50.dp))
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//        OutlinedTextField(
//            modifier = Modifier.fillMaxWidth(0.9F),
//            value = password.value,
//            onValueChange = { passwordString -> password.value = passwordString },
//            label = {Text(text = "Password") },
//            visualTransformation = PasswordVisualTransformation(),
//            shape = RoundedCornerShape(corner = CornerSize(50.dp))
//
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        Button(
//            modifier = Modifier.fillMaxWidth(0.9F).height(50.dp),
//            onClick = { Logged(username.value, password.value, context, navController) },
//            shape = RoundedCornerShape(corner = CornerSize(50.dp))
//        ) {
//            Text(text = "Login")
//        }
    }
}

fun handleSignInResult(task: Task<GoogleSignInAccount>, navController: NavController) {
    try {
        val account = task.result
        println("Sign in successful. ${task.result.displayName}")
        SharedPreferences(Graph.appContext).username = task.result.displayName!!
        SharedPreferences(Graph.appContext).photoUrl = task.result.photoUrl.toString()
        navController.navigate("home")
    } catch (e: Exception) {
        println("Sign in failed. ${task?.result.displayName}")
    }

}

fun Logged(username: String, password: String, context: Context, navController: NavController) {

    val creds = SharedPreferences(context)

    if (username == creds.username && password == creds.password || username == "" && password == "") {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        navController.navigate("home")
    } else {
        Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
    }
}