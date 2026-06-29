package com.app.lokalmarket.v1.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.v1.ui.main.MainActivity
import com.app.lokalmarket.v1.data.model.ApiResponse
import com.app.lokalmarket.v1.data.model.LoginRequest
import com.app.lokalmarket.v1.data.model.Pengguna
import com.app.lokalmarket.v1.data.remote.ApiClient
import com.app.lokalmarket.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                binding.btnLogin.isEnabled = false
                binding.btnLogin.text = "Loading..."

                val request = LoginRequest(email, password)

                ApiClient.apiService.login(request).enqueue(object : Callback<ApiResponse<Pengguna>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Pengguna>>,
                        response: Response<ApiResponse<Pengguna>>
                    ) {
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = "Login"

                        if (response.isSuccessful && response.body()?.success == true) {

                            val pengguna = response.body()?.data

                            if (pengguna != null) {
                                val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
                                val editor = sharedPref.edit()

                                editor.putInt("USER_ID", pengguna.id)
                                editor.putString("USER_NAME", pengguna.nama)
                                editor.putString("USER_EMAIL", pengguna.email)
                                editor.putBoolean("IS_LOGGED_IN", true)
                                editor.apply()

                                Toast.makeText(this@LoginActivity, "Selamat datang, ${pengguna.nama}!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            val pesanError = response.body()?.message ?: "Password atau email salah, silahkan coba lagi."
                            Toast.makeText(this@LoginActivity, pesanError, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<Pengguna>>, t: Throwable) {
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = "Login"
                        Toast.makeText(this@LoginActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Silakan isi email dan password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}