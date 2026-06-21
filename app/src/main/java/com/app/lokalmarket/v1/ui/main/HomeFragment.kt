package com.app.lokalmarket.v1.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.lokalmarket.databinding.FragmentHomeBinding
import com.app.lokalmarket.v1.data.model.Produk
import com.app.lokalmarket.v1.ui.adapter.ProdukCardAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyList = mutableListOf(
            Produk(1, "Produk 1", "Deskripsi", 15000, null, 0),
            Produk(2, "Produk 2", "Deskripsi", 25000, null, 10)
        )

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = ProdukCardAdapter(dummyList) { produkTerpilih, imageRes ->

            val intent = Intent(requireContext(), DetailProdukActivity::class.java)
            intent.putExtra("EXTRA_ID", produkTerpilih.id)
            intent.putExtra("EXTRA_NAME", produkTerpilih.namaProduk)
            intent.putExtra("EXTRA_PRICE", produkTerpilih.harga)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}