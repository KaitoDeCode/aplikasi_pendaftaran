package com.example.pendaftaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pendaftaran.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = ActivityRegisterBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this@Register,MainActivity::class.java))
        }

        binding.registerBtn.setOnClickListener {
            GlobalScope.launch (Dispatchers.IO){
                Log.d("TESSS", "onCreate: TES BROWh")
                var url = URL("http://10.0.2.2:5059/api/Pendaftaran/register")
                var conn =  url.openConnection() as HttpURLConnection
                conn.setRequestProperty("Content-Type","application/json")
                conn.requestMethod= "POST"

                var data = JSONObject().apply {
                    put("id",0)
                    put("username", binding.username.text)
                    put("email", binding.editTextTextEmailAddress.text)
                    put("password", binding.editTextTextPassword.text)
                    put("status",null)
                }

                conn.outputStream.write(data.toString().toByteArray())
                Log.d("TESS lagi ", "onCreate: ${data} ")

                var statusCode = conn.responseCode
                Log.d("statusCOde", "onCreate: ${statusCode}")

                if(statusCode == 201 || statusCode == 200) {
                    runOnUiThread{
                        Toast.makeText(this@Register, "Selamat datang",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Register,MainActivity::class.java))
                    }
                }else{
                    var result = conn.errorStream.bufferedReader().readText()
                    Log.d("RESULT", "onCreate: ${result}")
                    var error = JSONObject(result)
                    Log.d("Error", "onCreate: ${error.toString()}")
                    runOnUiThread{
                        Toast.makeText(this@Register," Gagal Authentikasi", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}