package com.example.teste.ui.screen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teste.api.DailyMotion
import com.example.teste.api.RetrofitInstance
import com.example.teste.model.TokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun VideoUploader(api: DailyMotion, context: Context) {
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadStatus by remember { mutableStateOf("") }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedVideoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { videoPickerLauncher.launch("video/*") }) {
            Text("Selecionar Vídeo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        selectedVideoUri?.let {
            Text("Vídeo selecionado: $it")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isUploading = true
                    uploadStatus = ""
                    uploadVideo(it, api, context) { status ->
                        isUploading = false
                        uploadStatus = status
                    }
                },
                enabled = !isUploading
            ) {
                Text("Upload")
            }
        }

        if (isUploading) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(uploadStatus)
    }
}

private fun uploadVideo(
    videoUri: Uri,
    api: DailyMotion,
    context: Context,
    onResult: (String) -> Unit
) {
    val file = createFileFromUri(context, videoUri)
    if (file == null) {
        onResult("Erro ao acessar o arquivo de vídeo.")
        return
    }

    if(!file.extension.equals("mp4", ignoreCase = true)) {
        onResult("Formato de arquivo inválido. Apenas arquivos .mp4 são aceitos.")
        Log.e("UploadVideo", "Formato de arquivo inválido: ${file.extension}")
        return
    }
    Log.d("PASSOU","É .mp4")

    CoroutineScope(Dispatchers.IO).launch {
        try {
            // Passo 1: Autenticando
            val tokenReq = TokenRequest(
                grant_type = "password",
                client_id = "05d28f848a974b520ac3",
                client_secret = "9e390312bc71cb2013f068511f185f46f6dbbdaf",
                username = "qufc",
                password = "ufcq1010.",
                scope = "manage_videos userinfo"
            )

            val tokenResponse = api.authenticate(
                grantType = tokenReq.grant_type,
                clientId = tokenReq.client_id,
                clientSecret = tokenReq.client_secret,
                username = tokenReq.username,
                password = tokenReq.password,
                scope = tokenReq.scope
            ).access_token

            Log.d("TOKEN", tokenResponse)

            // Passo 2: Obter URL de Upload
            val uploadUrlResponse = api.getUploadUrl("Bearer $tokenResponse")
            val uploadUrl = uploadUrlResponse.upload_url

            Log.d("URL", uploadUrl)

            //Passo 3: Upload do vídeo
            val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
            val videoPart = MultipartBody.Part.createFormData("file", file.name, requestFile)
            api.uploadVideo(uploadUrl, videoPart)

            onResult("Upload realizado com sucesso")
        } catch (e: Exception) {
            onResult("Erro: ${e.message}")
        }
    }
}

private fun createFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".mp4", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        outputStream.close()
        inputStream?.close()
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
