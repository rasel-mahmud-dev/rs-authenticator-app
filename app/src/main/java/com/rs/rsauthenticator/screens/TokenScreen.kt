package com.rs.rsauthenticator.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.R
import com.rs.rsauthenticator.components.AuthenticatorItem
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.state.AccountState
import com.rs.rsauthenticator.state.AuthState
import com.rs.rsauthenticator.utils.generateTOTP
import kotlinx.coroutines.delay
import java.time.LocalDateTime


@Composable
fun TokenScreen(
    navController: NavHostController,
    onShowBottomSheet: () -> Unit
) {

    val context = LocalContext.current
    val dbHelper = remember { TotpDatabaseHelper.getInstance(context) }
    var remainingTime by remember { mutableIntStateOf(LocalDateTime.now().second) }
    val auth = AuthState.auth

    LaunchedEffect(Unit) {
        AccountState.loadItems(dbHelper)
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            remainingTime = LocalDateTime.now().second

            if (remainingTime % 30 == 0) {
                AccountState.updateAll(AccountState.items.map { it.copy(otpCode = generateTOTP(
                    it.secret,
                    it.algorithm
                )) })
                AccountState.items.forEach {
                    dbHelper.updateTotpEntry(
                        id = it.id,
                        newOtp = it.otpCode
                    )
                }
            }
//            Log.d("Timer", "Remaining Time: $remainingTime")
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp),
    ) {

        RsColumn(
            Modifier
                .zIndex(100F)
                .align(Alignment.BottomEnd), py = 80.dp, px = 10.dp
        ) {
            PrimaryButton(
                onClick = {
                    onShowBottomSheet()
                },
                modifier = Modifier
                    .zIndex(100F)
                    .padding(3.dp),
                label = if (AccountState.items.isEmpty()) "New Connection" else "",
                iconSize = 18.sp,
                icon = "\u002b",
            )
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    RsRow(
                        modifier = Modifier
                            .weight(1f)
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable {
                                navController.navigate("settings/profile")
                            },

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),

                        ) {

                        Image(
                            painter = rememberAsyncImagePainter(if (auth?.avatar.isNullOrEmpty()) R.drawable.avatar else auth?.avatar),
                            contentDescription = "Rs Authenticator Logo",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )

                        Column {
                            CustomText(
                                modifier = Modifier,
                                text = auth?.username ?: "Guest.",
                                fs = 16.sp,
                                pt = 5.dp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            CustomText(
                                modifier = Modifier,
                                text = auth?.email ?: "guest@example.com",
                                fs = 14.sp,
                                pt = 5.dp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        }
                    }

                    RsRow {
                        PrimaryButton(
                            modifier = Modifier
                                .scale(0.8F)
                                .zIndex(100F),
                            iconSize = 16.sp,
                            onClick = {
                                if (auth == null) {
                                    navController.navigate("login")
                                } else {
                                    AuthState.clearAuthInfo(context)
                                }

                            },
                            label = if (auth == null) "Login" else "Logout",
                            icon = "\uf2f6"
                        )
                    }
                }
            }

            RsColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                AccountState.items.forEach {
                    SwipeItem(it.id) {
                        AuthenticatorItem(it, remainingTime)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeItem(id: String, content: @Composable RowScope.() -> Unit) {
    val context = LocalContext.current
    val dbHelper = remember { TotpDatabaseHelper.getInstance(context) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { direction ->
            when (direction) {
                SwipeToDismissBoxValue.StartToEnd, SwipeToDismissBoxValue.EndToStart -> {
                    AccountState.removeItem(dbHelper, id)
                    Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                    true
                }

                SwipeToDismissBoxValue.Settled -> false
            }
        },
        positionalThreshold = { it * 0.25f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = Modifier,
        backgroundContent = {},
        content = {
            content()
        }
    )
}