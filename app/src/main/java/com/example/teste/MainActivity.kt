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
            val api = RetrofitInstance.instance
            VideoUploader(api, this)
        }
    }
}
