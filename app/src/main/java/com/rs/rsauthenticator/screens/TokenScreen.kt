package com.rs.rsauthenticator.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.components.AuthenticatorItem
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.state.AccountState


@Composable
fun TokenScreen(navController: NavHostController) {

    val context = LocalContext.current
    val dbHelper = remember { TotpDatabaseHelper.getInstance(context) }

    val entries = AccountState.items

    LaunchedEffect(Unit) {
        AccountState.loadItems(dbHelper)
    }


    RsColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        RsColumn(
            modifier = Modifier.fillMaxWidth(),
            py = 16.dp,
            px = 0.dp,
        ) {


            RsRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Image(
                    painter = rememberAsyncImagePainter("https://avatars.githubusercontent.com/u/99707905?v=4"),
                    contentDescription = "Rs Authenticator Logo",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )

                RsColumn {
                    CustomText(
                        modifier = Modifier,
                        text = "Rs Authenticator",
                        fs = 16.sp,
                        pt = 5.dp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    CustomText(
                        modifier = Modifier,
                        text = "rasel.mahmud.dev@gmail.com",
                        fs = 14.sp,
                        pt = 5.dp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }

            }

//            RsRow {
//                CustomText("sasdddddddddddddddddf")
//            }
        }

        RsColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            entries.forEach {
                AuthenticatorItem(it)
            }
        }


//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(entries) { entry ->
//
//            }
//        }
    }
}

