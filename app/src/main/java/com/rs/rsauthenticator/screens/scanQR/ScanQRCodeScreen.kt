package com.rs.rsauthenticator.screens.scanQR

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsIconButton
import com.rs.rsauthenticator.components.RsRow


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
//            Log.e("QRScanner", "Scanning failed: ${it.message}")
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}


@Composable
fun ScanQRCodeScreen(
    onQRCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var cameraSelector3 by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    LaunchedEffect(Unit, cameraSelector3) {
        val cameraProvider = cameraProviderFuture.get()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(cameraSelector3)
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
                        onQRCodeScanned(qrCode)
                    }
                }
            }
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
    }

    RsColumn(Modifier.padding(16.dp, 0.dp)) {
        RsColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            pb = 10.dp,
        ) {
            CustomText(
                textAlign = TextAlign.Center,
                text = "Scan Code",
                fontWeight = FontWeight.SemiBold
            )
        }

        Box(modifier = Modifier.height(40.dp)) {}


        Box(modifier = Modifier) {

            RsRow(
                modifier = Modifier
                .zIndex(100F)
                .align(Alignment.TopEnd),
                px=16.dp,
                py=16.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp) // make it short using spaceX property
            ) {

//                RsIconButton(
//                    modifier = Modifier.zIndex(200F),
//                    onClick = {},
//                    bgColor = Color(0xFFF44336),
//                    w = 32.dp,
//                    h = 32.dp,
//                    radius = 40.dp
//                ) {
//                    CustomText(icon = "\ue0b8", color = Color.White)
//                }

                RsIconButton(
                    modifier = Modifier.zIndex(200F),
                    onClick = {
                        if (cameraSelector3 == CameraSelector.LENS_FACING_FRONT) {
                            cameraSelector3 = CameraSelector.LENS_FACING_BACK
                        } else {
                            cameraSelector3 = CameraSelector.LENS_FACING_FRONT
                        }
                    },
                    bgColor = Color(0xFFF44336),
                    w = 32.dp,
                    h = 32.dp,
                    radius = 40.dp
                ) {
                    CustomText(icon = "\uf030", color = Color.White)
                }

            }

            RsColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
            ) {


                AndroidView(
                    { previewView },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                )

            }
        }

        RsColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            py = 20.dp
        ) {
            CustomText(
                fs = 14.sp,
                text = "Scanned by Rs authenticator.",
                fontWeight = FontWeight.Normal,
                color = Color(0xFFA6A6A6)
            )
        }
    }
}
