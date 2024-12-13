package com.example.teste
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.teste.api.RetrofitInstance
import com.example.teste.ui.screen.VideoUploader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val api = RetrofitInstance.api
            VideoUploader(api, "SEU_ACCESS_TOKEN", this)
        }
    }
}
