package com.rs.rsauthenticator.screens

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsIconButton



@OptIn(ExperimentalGetImage::class)
fun processImageProxy(imageProxy: ImageProxy, onQrCodeScanned: (String) -> Unit) {
    val image = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
    val scanner = BarcodeScanning.getClient()
    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            barcodes.firstOrNull()?.rawValue?.let { qrCode ->
                onQrCodeScanned(qrCode)
            }
        }
        .addOnFailureListener {
            Log.e("QRScanner", "Scanning failed: ${it.message}")
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}


@Composable
fun ScanQRCodeScreen(navController: NavHostController, onQRCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var scannedCode by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    processImageProxy(imageProxy) { qrCode ->
                        scannedCode = qrCode
                        onQRCodeScanned(qrCode)
                        navController.popBackStack()
                    }
                }
            }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
    }

    Box(modifier = Modifier.fillMaxSize()) {


        Row(
            modifier = Modifier
                .zIndex(200F)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            RsIconButton(
                modifier = Modifier,
                onClick = {},
                bgColor = Color(0xFFF44336),
                w = 32.dp,
                h = 32.dp
            ) {
                CustomText(icon = "\uf00d")
            }

            CustomText(
                textAlign = TextAlign.Center,
                text = "Scan Code",
                fontWeight = FontWeight.SemiBold
            )

            RsIconButton(
                modifier = Modifier.zIndex(200F),
                onClick = {},
                bgColor = Color(0xFFF44336),
                w = 32.dp,
                h = 32.dp
            ) {
                CustomText(icon = "\ue0b8")
            }
        }

        Column(
            modifier = Modifier
                .zIndex(200F)
                .align(Alignment.BottomCenter)
                .padding(10.dp)
        ) {
            CustomText(
                fs = 14.sp,
                textAlign = TextAlign.Center,
                text = "Scanned by Rs authenticator.",
                fontWeight = FontWeight.Normal,
                color = Color(0xFFA6A6A6)
            )
        }

        Column(modifier = Modifier) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        }
    }
}
