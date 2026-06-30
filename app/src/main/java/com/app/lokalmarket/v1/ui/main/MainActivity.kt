package com.app.lokalmarket.v1.ui.main

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.lokalmarket.R
import com.app.lokalmarket.databinding.ActivityMainBinding
import com.app.lokalmarket.v1.receiver.NetworkReceiver

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val networkReceiver = NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), addToBackStack = false)
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_cart -> loadFragment(KeranjangFragment())
                R.id.nav_history -> loadFragment(RiwayatFragment())
            }
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })
    }

    /**
     * Dipanggil dari tombol back custom di toolbar fragment (Keranjang, Riwayat, dll).
     * Mundur satu langkah sesuai backstack, bukan langsung ke Home.
     */
    fun navigateBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: IllegalArgumentException) {
        }
    }
}