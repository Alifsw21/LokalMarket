package com.app.lokalmarket.v1.ui.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.lokalmarket.databinding.FragmentHomeBinding
import com.app.lokalmarket.v1.data.model.Produk
import com.app.lokalmarket.v1.ui.adapter.ProdukCardAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var produkAdapter: ProdukCardAdapter

    // Sumber data asli, tidak boleh dipakai langsung sebagai referensi adapter
    private val dummyList = mutableListOf(
        Produk(1, "Sepatu Anti Mainstream", "sepatu ini sangat bagus dipakai untuk menggapai mimpi yang belum tercapai", 15000000, null, 0),
        Produk(2, "T-shirt keep going on", "baju ini sangat sejuk dipakai, penggunanya akan merasa seolah berada di surga", 250000, null, 10)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyStatusBarPadding()

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)

        // PENTING: kirim salinan list (toMutableList), bukan referensi dummyList langsung,
        // supaya dummyList tidak ikut berubah saat adapter melakukan clear/addAll
        produkAdapter = ProdukCardAdapter(items = dummyList.toMutableList()) { produkTerpilih, imageRes ->
            val intent = Intent(requireContext(), DetailProdukActivity::class.java)
            intent.putExtra("EXTRA_ID", produkTerpilih.id)
            intent.putExtra("EXTRA_NAME", produkTerpilih.namaProduk)
            intent.putExtra("EXTRA_PRICE", produkTerpilih.harga)
            intent.putExtra("EXTRA_DESC", produkTerpilih.deskripsi)
            intent.putExtra("EXTRA_IMAGE", imageRes)
            startActivity(intent)
        }
        binding.rvProducts.adapter = produkAdapter

        setupSearch()
        setupProfile()
    }

    private fun applyStatusBarPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootHome) { v, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, statusBarInset.top, v.paddingRight, v.paddingBottom)
            insets
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s?.toString().orEmpty().trim()
                val filtered = if (keyword.isEmpty()) {
                    dummyList
                } else {
                    dummyList.filter { it.namaProduk.contains(keyword, ignoreCase = true) }
                }
                produkAdapter.updateData(filtered)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupProfile() {
        binding.ivProfile.setOnClickListener {
            val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            val buyerName = sharedPref.getString("USER_NAME", null)

            val message = if (buyerName.isNullOrBlank()) {
                "Status: Guest\nKamu belum login dengan akun terdaftar."
            } else {
                "Nama: $buyerName\nStatus: Pengguna"
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Informasi Akun")
                .setMessage(message)
                .setPositiveButton("Tutup", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}