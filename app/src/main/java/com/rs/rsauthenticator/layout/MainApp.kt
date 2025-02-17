package com.rs.rsauthenticator.layout

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.navigation.compose.rememberNavController
import com.rs.rsauthenticator.AppNavigation


@Composable
fun MainApp(applicationContext: Context) {


    val navController = rememberNavController()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFF0303))
    ) {

//        MainSideBar(navController)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)),
            contentAlignment = Alignment.Center
        ) {

            AppNavigation(applicationContext, navController)
        }
    }

//    Column(
//        modifier = Modifier
//            .padding(16.dp)
//            .background(Color(0xFF171717))
//    ) {
//
//        TextField(
//            value = ipv4Address,
//            onValueChange = { ipv4Address = it },
//            label = { Text("Enter IPv4 Address") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            singleLine = true
//        )
//
//
//
//        Button(onClick = {
//            sendMouseClickEvent()
//        }) {
//            Text("Add")
//        }
//
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Button(onClick = {
//                startConnectionFromServer()
//            }) {
//                Icon(
//                    imageVector = Icons.Default.PlayArrow,
//                    contentDescription = "Start Icon",
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Start")
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Button(onClick = {
//                disconnectFromServer()
//            }) {
//                Icon(
//                    imageVector = Icons.Default.PlayArrow,
//                    contentDescription = "Start Icon",
//                    modifier = Modifier.size(24.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Stop")
//            }
//        }

//    }
}


