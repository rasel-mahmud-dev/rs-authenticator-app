package com.rs.rsauthenticator.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.R
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.state.SharePref
import kotlin.math.abs


data class CarouselItem(
    val id: Int,
    val description: String,
    val title: String,
    val imageRes: Int
)

val images = listOf(
    CarouselItem(
        id = 1,
        title = "Secure Your Accounts",
        description = "Enhance your online security by using two-factor authentication to protect your accounts from unauthorized access.",
        imageRes = R.drawable.failure
    ),
    CarouselItem(
        id = 2,
        title = "Easy Backup & Restore",
        description = "Easily backup and restore your authentication data so you never lose access to your accounts.",
        imageRes = R.drawable.log_in
    ),
    CarouselItem(
        id = 3,
        title = "Scan & Authenticate",
        description = "Quickly scan QR codes to add new authentication tokens with ease.",
        imageRes = R.drawable.failure
    ),
    CarouselItem(
        id = 4,
        title = "Stay in Control",
        description = "Manage your accounts efficiently with a simple and user-friendly interface.",
        imageRes = R.drawable.log_in
    )
)

@Composable
fun TourScreen(navController: NavHostController) {

    val applicationContext = LocalContext.current

    val sharePref = SharePref.getInstance(applicationContext)

    val pagerState = rememberPagerState(initialPage = 0) { 2 }

    val item = images[pagerState.currentPage]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        RsColumn {
            CustomText(
                fontWeight = FontWeight.SemiBold,
                fs = 24.sp,
                text = item.title,
                color = Color.Black
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x00F3F3F3))
                .height(350.dp)
        ) { page ->

            val item = images[page]

            Image(
                painter = rememberAsyncImagePainter(item.imageRes),
                contentDescription = "Tour Image",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(250.dp)
                    .graphicsLayer {
                        val pageOffset =
                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        alpha = 1f - abs(pageOffset * 0.4f)
                        scaleX = 1f - abs(pageOffset * 0.2f)
                        scaleY = 1f - abs(pageOffset * 0.2f)
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        RsColumn(px = 16.dp, py = 0.dp) {
            CustomText(
                fs = 16.sp,
                textAlign = TextAlign.Center,
                text = item.description,
                color = Color.Gray,
            )

            RsRow(
                pt = 30.dp,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                PrimaryButton(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            pagerState.requestScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    label = "Prev"
                )


                PrimaryButton(
                    onClick = {
                        if (pagerState.currentPage == pagerState.pageCount - 1) {
                            sharePref.setAppInitialized(true)
                            navController.navigate("home")
                        } else {
                            pagerState.requestScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    label = if (pagerState.currentPage == pagerState.pageCount - 1) "Completed" else "Next"
                )

            }

        }
    }
}

@Preview(
    device = "spec:width=1080px,height=2340px,dpi=440", showSystemUi = true,
    showBackground = true
)
@Composable
fun PreviewTourScreen() {
    val navController = NavHostController(context = LocalContext.current)
    TourScreen(navController = navController)
}