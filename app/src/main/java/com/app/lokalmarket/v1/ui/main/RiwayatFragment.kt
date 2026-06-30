package com.app.lokalmarket.v1.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lokalmarket.v1.data.model.ApiResponse
import com.app.lokalmarket.v1.data.model.Pesanan
import com.app.lokalmarket.v1.data.remote.ApiClient
import com.app.lokalmarket.v1.ui.adapter.PesananListAdapter
import com.app.lokalmarket.databinding.FragmentRiwayatBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding

    private lateinit var adapter: PesananListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyStatusBarPadding()
        setupToolbar()
        setupRecyclerView()
        getRiwayat()
    }

    private fun applyStatusBarPadding() {
        val root = binding?.rootRiwayat ?: return
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, statusBarInset.top, v.paddingRight, v.paddingBottom)
            insets
        }
    }

    private fun setupToolbar() {
        binding?.toolbarRiwayat?.setNavigationOnClickListener {
            (activity as? MainActivity)?.navigateBack()
        }
    }

    private fun setupRecyclerView() {
        adapter = PesananListAdapter(
            mutableListOf(),
            onItemClick = { pesanan ->
                val intent = Intent(requireContext(), DetailPesananActivity::class.java).apply {
                    putExtra("EXTRA_ID_PESANAN", pesanan.id)
                    putExtra("EXTRA_TANGGAL", pesanan.tanggalPesanan)
                    putExtra("EXTRA_TOTAL", pesanan.totalHarga)
                    putExtra("EXTRA_ALAMAT", pesanan.alamat)
                }
                startActivity(intent)
            },
            onDeleteClick = { pesanan ->
                hapusPesanan(pesanan)
            }
        )
        binding?.rvRiwayat?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvRiwayat?.adapter = adapter
    }

    private fun getRiwayat() {
        if (_binding == null) return
        binding?.pbRiwayat?.visibility = View.VISIBLE
        binding?.tvEmptyRiwayat?.visibility = View.GONE

        ApiClient.apiService.getRiwayatPesanan().enqueue(object : Callback<ApiResponse<List<Pesanan>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Pesanan>>>,
                response: Response<ApiResponse<List<Pesanan>>>
            ) {
                if (_binding == null || !isAdded) return
                binding?.pbRiwayat?.visibility = View.GONE

                if (response.isSuccessful && response.body()?.success == true) {
                    val list = response.body()?.data ?: emptyList()
                    if (list.isEmpty()) {
                        binding?.tvEmptyRiwayat?.visibility = View.VISIBLE
                    } else {
                        adapter.updateData(list)
                    }
                } else {
                    Toast.makeText(context, "Gagal memuat riwayat", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Pesanan>>>, t: Throwable) {
                if (_binding == null || !isAdded) return
                binding?.pbRiwayat?.visibility = View.GONE
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hapusPesanan(pesanan: Pesanan) {
        ApiClient.apiService.hapusPesanan(pesanan.id).enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Pesanan #${pesanan.id} berhasil dihapus", Toast.LENGTH_SHORT).show()
                    adapter.removeItem(pesanan)
                    if (adapter.itemCount == 0) {
                        binding?.tvEmptyRiwayat?.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal menghapus pesanan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}