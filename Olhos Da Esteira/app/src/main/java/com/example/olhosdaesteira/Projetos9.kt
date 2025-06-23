package com.example.olhosdaesteira

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.olhosdaesteira.ui.theme.OlhosDaEsteiraTheme
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.ktx.storage
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import androidx.camera.core.Preview as CameraPreview

class Projetos9Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OlhosDaEsteiraTheme {
                Projetos9()
            }
        }
    }
}

@Composable
fun Projetos9() {
    val context = LocalContext.current
    var temPermissao by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        temPermissao = granted
    }

    LaunchedEffect(true) {
        if (!temPermissao) launcher.launch(android.Manifest.permission.CAMERA)
    }

    if (temPermissao) {
        TelaCamera()
    } else {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Permissão da câmera é necessária.")
        }
    }
}
@Composable
fun TelaCamera() {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val previewView = remember { PreviewView(context) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    LaunchedEffect(true) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = CameraPreview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        Button(
            onClick = {
                val file = File(
                    context.cacheDir,
                    "foto_${System.currentTimeMillis()}.jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val fileUri = Uri.fromFile(file)

                            try {
                                val inputStream = context.contentResolver.openInputStream(fileUri)
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                inputStream?.close()

                                // Redimensiona a imagem para o tamanho esperado pelo modelo
                                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 478, 850, true)

                                val input = Array(1) { Array(478) { Array(850) { FloatArray(3) } } }

                                for (y in 0 until 850) {
                                    for (x in 0 until 478) {
                                        val pixel = resizedBitmap.getPixel(x, y)

                                        val r = (pixel shr 16 and 0xFF) / 255.0f
                                        val g = (pixel shr 8 and 0xFF) / 255.0f
                                        val b = (pixel and 0xFF) / 255.0f

                                        input[0][x][y][0] = r
                                        input[0][x][y][1] = g
                                        input[0][x][y][2] = b
                                    }
                                }

                                // Carrega o modelo TFLite
                                val assetFileDescriptor = context.assets.openFd("modelo.tflite")
                                val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
                                val fileChannel = fileInputStream.channel
                                val startOffset = assetFileDescriptor.startOffset
                                val declaredLength = assetFileDescriptor.declaredLength
                                val modelBuffer = fileChannel.map(
                                    FileChannel.MapMode.READ_ONLY,
                                    startOffset,
                                    declaredLength
                                )

                                val interpreter = Interpreter(modelBuffer)

                                // Prepara a saída
                                val output = Array(1) { FloatArray(3) }  // Saída com 2 classes
                                interpreter.run(input, output)

                                // Pega o índice da classe com maior confiança
                                val resultado = output[0].withIndex().maxByOrNull { it.value }
                                Toast.makeText(
                                    context,
                                    "Classe detectada: ${if(resultado?.index==1) "Laranja" else "Verde"} ",
                                    Toast.LENGTH_LONG
                                ).show()

                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, "Erro ao processar modelo: ${e.message}", Toast.LENGTH_LONG).show()
                            }

                            // Upload para o Firebase Storage
                            /*val storageRef = com.google.firebase.ktx.Firebase.storage.reference
                            val imageRef = storageRef.child("imagens/${file.name}")

                            imageRef.putFile(fileUri)
                                .addOnSuccessListener {
                                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                                        val downloadUrl = uri.toString()
                                        val ref = Firebase.database.getReference("fotos")
                                        ref.push().setValue(downloadUrl)
                                        Toast.makeText(context, "Imagem salva no banco!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    //Toast.makeText(context, "Erro ao enviar imagem: ${exception.message}", Toast.LENGTH_LONG).show()
                                    exception.printStackTrace()
                                }*/
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(context, "Erro: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("Capturar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Projetos9Preview() {
    OlhosDaEsteiraTheme {
        Projetos9()
    }
}
