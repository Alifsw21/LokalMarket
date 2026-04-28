package com.app.lokalmarket.v1.ui.main

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.lokalmarket.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFullName = findViewById<EditText>(R.id.et_full_name)
        val etEmail = findViewById<EditText>(R.id.et_email_reg)
        val etPassword = findViewById<EditText>(R.id.et_password_reg)
        val btnRegister = findViewById<Button>(R.id.btn_register)
        val tvBackToLogin = findViewById<TextView>(R.id.tv_back_to_login)

        btnRegister.setOnClickListener {
            val name = etFullName.text.toString()
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()) {
                Toast.makeText(this, "Akun $name berhasil dibuat!", Toast.LENGTH_LONG).show()

                finish()
            } else {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            }
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}