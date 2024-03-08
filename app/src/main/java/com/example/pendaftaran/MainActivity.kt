package com.example.pendaftaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.pendaftaran.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                val url = URL("http://10.0.2.2:5059/api/Pendaftaran/login")
                var conn = url.openConnection() as HttpURLConnection
                conn.requestMethod= "POST"
                conn.setRequestProperty("Content-Type","application/json")
                val body = JSONObject().apply{
                    put("email",binding.editTextTextEmailAddress.text)
                    put("password",binding.editTextTextPassword.text)
                }
                conn.outputStream.write(body.toString().toByteArray())

                var statusCode = conn.responseCode;
                Log.d("statusCOde", "onCreate: $statusCode")
                if(statusCode == 201 || statusCode == 200){
                    var result = conn.inputStream.bufferedReader().readText()
                    var user = JSONObject(result)

                    var editor = getSharedPreferences("random", MODE_PRIVATE).edit()
                    editor.putString("user", result)
                    editor.apply()

                    runOnUiThread{
                        Toast.makeText(this@MainActivity, "Selamat datang",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity,Dashboard::class.java))
                    }

                }else{


                    var result = conn.errorStream.bufferedReader().readText()
                    Log.d("Error", "onCreate: ${result}")

                    runOnUiThread{
                        Toast.makeText(this@MainActivity," Gagal Authentikasi, karena $result", Toast.LENGTH_SHORT).show()
                    }



                }


            }
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(this@MainActivity, Register::class.java))
        }

    }
}
