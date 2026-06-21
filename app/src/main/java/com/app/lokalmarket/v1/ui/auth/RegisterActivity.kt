package com.app.lokalmarket.v1.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.databinding.ActivityRegisterBinding
import com.app.lokalmarket.v1.data.model.ApiResponse
import com.app.lokalmarket.v1.data.model.RegisterRequest
import com.app.lokalmarket.v1.data.remote.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val nama = binding.etFullName.text.toString().trim()
            val email = binding.etEmailReg.text.toString().trim()
            val password = binding.etPasswordReg.text.toString().trim()

            if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

                binding.btnRegister.isEnabled = false
                binding.btnRegister.text = "Loading..."

                val request = RegisterRequest(nama, email, password)

                ApiClient.apiService.register(request).enqueue(object: Callback<ApiResponse<Any>> {
                    override fun onResponse(
                        call: Call<ApiResponse<Any>>,
                        response: Response<ApiResponse<Any>>
                    ) {
                        binding.btnRegister.isEnabled = true
                        binding.btnRegister.text = "Daftar"

                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@RegisterActivity, "Registrasi berhasil! Silakan Login.", Toast.LENGTH_SHORT).show()

                            finish()
                        } else {
                            val pesanError = response.body()?.message ?: "Registrasi gagal."
                            Toast.makeText(this@RegisterActivity, pesanError, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                        binding.btnRegister.isEnabled = true
                        binding.btnRegister.text = "Daftar"
                        Toast.makeText(this@RegisterActivity, "Error koneksi: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })

            } else {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}