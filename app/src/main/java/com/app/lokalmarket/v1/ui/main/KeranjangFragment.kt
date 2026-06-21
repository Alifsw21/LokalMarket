package com.app.lokalmarket.v1.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lokalmarket.databinding.FragmentKeranjangBinding
import com.app.lokalmarket.v1.data.local.KeranjangDbHelper
import com.app.lokalmarket.v1.ui.adapter.KeranjangListAdapter
import java.text.NumberFormat
import java.util.Locale

class KeranjangFragment : Fragment() {

    private var _binding: FragmentKeranjangBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: KeranjangDbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = KeranjangDbHelper(requireContext())
        loadKeranjang()
    }

    override fun onResume() {
        super.onResume()
        loadKeranjang()
    }

    private fun loadKeranjang() {
        val items = dbHelper.getAllKeranjang()
        val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val totalHarga = items.sumOf { it.hargaSatuan * it.jumlahBarang }

        if (items.isEmpty()) {
            binding.tvCartEmpty.visibility = View.VISIBLE
            binding.rvCartItems.visibility = View.GONE
        } else {
            binding.tvCartEmpty.visibility = View.GONE
            binding.rvCartItems.visibility = View.VISIBLE

            binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
            binding.rvCartItems.adapter = KeranjangListAdapter(items.toMutableList()) { itemYangDihapus ->
                dbHelper.deleteKeranjang(itemYangDihapus.id)
                loadKeranjang()
            }
        }

        binding.tvTotalPrice.text = rupiah.format(totalHarga)

        binding.btnCheckoutFromCart.setOnClickListener {
            if (items.isEmpty()) {
                Toast.makeText(requireContext(), "Keranjang masih kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val buyerName = sharedPref.getString("USER_NAME", "Pembeli")

            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            intent.putExtra("EXTRA_FROM_CART", true)
            intent.putExtra("EXTRA_NAME", buyerName)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}