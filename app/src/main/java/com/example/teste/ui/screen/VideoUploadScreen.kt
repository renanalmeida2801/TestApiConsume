package com.example.teste.ui.screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teste.api.ApiDailyMotion
import com.example.teste.api.DailyMotion
import com.example.teste.api.RetrofitInstance
import com.example.teste.model.TokenRequest
import com.example.teste.model.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun VideoUploader(api: DailyMotion, accessToken: String, context: Context) {
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
                    uploadVideo(it, api, accessToken, context) { status ->
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
    context: Context,
    onResult: (String) -> Unit
) {
    val file = createFileFromUri(context, videoUri)
    if (file == null) {
        onResult("Erro ao acessar o arquivo de vídeo.")
        return
    }

    val tokenReq = TokenRequest(
        "password",
        "05d28f848a974b520ac3",
        "9e390312bc71cb2013f068511f185f46f6dbbdaf",
        "qufcprojeto@gmail.com",
        "ufcq1010.",
        "manage_videos userinfo"
        )


    CoroutineScope(Dispatchers.IO).launch {
        try {
            val tokenResponse = RetrofitInstance.instance.getAcessToken(tokenReq);
      /*


            // Passo 2: Obter a URL de upload
            val uploadUrlRequest = Request.Builder()
                .url("https://api.dailymotion.com/file/upload")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            val uploadUrlResponse = client.newCall(uploadUrlRequest).execute()
            if (!uploadUrlResponse.isSuccessful) throw Exception("Erro ao obter URL de upload")

            val uploadUrlJson = JSONObject(uploadUrlResponse.body?.string() ?: "")
            val uploadUrl = uploadUrlJson.getString("upload_url")

            // Passo 3: Fazer o upload do vídeo
            val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
            val videoPart = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val uploadRequest = Request.Builder()
                .url(uploadUrl)
                .post(videoPart.body)
                .build()

            val uploadResponse = client.newCall(uploadRequest).execute()
            if (!uploadResponse.isSuccessful) throw Exception("Erro ao fazer upload do vídeo")

            val uploadResultJson = JSONObject(uploadResponse.body?.string() ?: "")
            val videoUrl = uploadResultJson.getString("url")

            // Passo 4: Criar e publicar o vídeo
            val videoData = mapOf(
                "url" to videoUrl,
                "title" to "Meu vídeo",
                "channel" to "tech",
                "published" to "true",
                "is_created_for_kids" to "false"
            ).toQueryString().toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())

            val videoRequest = Request.Builder()
                .url("https://api.dailymotion.com/user/me/videos")
                .addHeader("Authorization", "Bearer $accessToken")
                .post(videoData)
                .build()

            val videoResponse = client.newCall(videoRequest).execute()
            if (!videoResponse.isSuccessful) throw Exception("Erro ao publicar vídeo")

            onResult("Upload e publicação realizados com sucesso!")*/
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

private fun Map<String, String>.toQueryString(): String {
    return this.map { "${it.key}=${it.value}" }.joinToString("&")
}
