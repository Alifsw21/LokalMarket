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
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lokalmarket.databinding.FragmentHomeBinding
import com.app.lokalmarket.v1.data.model.ApiResponse
import com.app.lokalmarket.v1.data.model.Kategori
import com.app.lokalmarket.v1.ui.adapter.KategoriCardAdapter
import com.app.lokalmarket.v1.data.model.Produk
import com.app.lokalmarket.v1.data.remote.ApiClient
import com.app.lokalmarket.v1.ui.adapter.ProdukCardAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var kategoriAdapter: KategoriCardAdapter
    private lateinit var produkAdapter: ProdukCardAdapter

    private var listProduk = mutableListOf<Produk>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupProdukAdapter()
        loadDataFromApi()
        applyStatusBarPadding()
        setupSearch()
        setupProfile()
    }

    private fun setupRecyclerView() {
        kategoriAdapter = KategoriCardAdapter(mutableListOf()) { kategoriTerpilih ->

            ApiClient.apiService.getProdukByKategori(kategoriTerpilih.id)
                .enqueue(object : Callback<ApiResponse<List<Produk>>> {
                    override fun onResponse(call: Call<ApiResponse<List<Produk>>>, response: Response<ApiResponse<List<Produk>>>) {
                        val data = response.body()?.data

                        if (response.isSuccessful && data != null) {
                            produkAdapter.updateData(data)
                        } else {
                            produkAdapter.updateData(emptyList())
                            Toast.makeText(context, "Belum ada produk di kategori ini", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<List<Produk>>>, t: Throwable) {
                        Toast.makeText(context, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.rvKategori.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = kategoriAdapter
        }
    }

    private fun setupProdukAdapter() {
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        produkAdapter = ProdukCardAdapter(mutableListOf()) { produkTerpilih, imageRes ->
            val intent = Intent(requireContext(), DetailProdukActivity::class.java).apply {
                putExtra("EXTRA_ID", produkTerpilih.id)
                putExtra("EXTRA_NAME", produkTerpilih.namaProduk)
                putExtra("EXTRA_IMAGE_ID", imageRes)
            }
            startActivity(intent)
        }
        binding.rvProducts.adapter = produkAdapter
    }

    private fun loadDataFromApi() {
        ApiClient.apiService.getAllKategori().enqueue(object: retrofit2.Callback<ApiResponse<List<Kategori>>> {
            override fun onResponse(call: retrofit2.Call<ApiResponse<List<Kategori>>>, response: retrofit2.Response<ApiResponse<List<Kategori>>>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { kategoriAdapter.updateData(it)}
                }
            }
            override fun onFailure(call: retrofit2.Call<ApiResponse<List<Kategori>>>, t: Throwable) {
                Toast.makeText(context, "Gagal mengambil data kategori", Toast.LENGTH_SHORT).show()
            }
        })

        ApiClient.apiService.getAllProduk().enqueue(object: retrofit2.Callback<ApiResponse<List<Produk>>> {
            override fun onResponse(call: retrofit2.Call<ApiResponse<List<Produk>>>, response: retrofit2.Response<ApiResponse<List<Produk>>>) {
                if (response.isSuccessful) {
                    listProduk = response.body()?.data?.toMutableList() ?: mutableListOf()
                    produkAdapter.updateData(listProduk)
                }
            }
            override fun onFailure(call: retrofit2.Call<ApiResponse<List<Produk>>>, t: Throwable) {
                Toast.makeText(context, "Gagal mengambil produk", Toast.LENGTH_SHORT).show()
            }
        })
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
                    listProduk
                } else {
                    listProduk.filter { it.namaProduk.contains(keyword, ignoreCase = true) }
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