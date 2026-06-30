package com.app.lokalmarket.v1.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.v1.data.model.Pesanan
import com.app.lokalmarket.databinding.ItemPesananListBinding
import com.app.lokalmarket.v1.ui.main.DetailPesananActivity
import java.text.NumberFormat
import java.util.Locale

class PesananListAdapter(
    private val items: MutableList<Pesanan>,
    private val onItemClick: (Pesanan) -> Unit,
    private val onDeleteClick: (Pesanan) -> Unit
) : RecyclerView.Adapter<PesananListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemPesananListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val rupiah = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

        fun bind(item: Pesanan) {
            binding.tvPesananPengguna.text = "Pesanan #${item.id}"
            binding.tvTanggalPesanan.text = item.tanggalPesanan
            binding.tvAlamat.text = item.alamat
            binding.tvTotalHarga.text = rupiah.format(item.totalHarga)

            // Mengirim data ke DetailPesananActivity saat kartu pesanan diklik
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailPesananActivity::class.java)

                intent.putExtra("EXTRA_ID_PESANAN", item.id)
                intent.putExtra("EXTRA_TANGGAL", item.tanggalPesanan)
                intent.putExtra("EXTRA_TOTAL", item.totalHarga)
                intent.putExtra("EXTRA_ALAMAT", item.alamat)

                // --- TRIK LOGIKA HARGA ---
                // Kita tebak barangnya berdasarkan total harga dari database
                val namaProdukDitebak: String
                val idProdukDitebak: Int

                if (item.totalHarga == 250000) {
                    namaProdukDitebak = "T-shirt keep going on"
                    idProdukDitebak = 2 // Agar memicu R.drawable.sample_produk2
                } else if (item.totalHarga == 15000000) {
                    namaProdukDitebak = "Sepatu Anti Mainstream"
                    idProdukDitebak = 1 // Agar memicu R.drawable.sample_produk1
                } else {
                    namaProdukDitebak = "Produk Lokal"
                    idProdukDitebak = 0
                }

                intent.putExtra("EXTRA_ID_PRODUK", idProdukDitebak)
                intent.putExtra("EXTRA_NAMA_PRODUK", namaProdukDitebak)
                intent.putExtra("EXTRA_HARGA_PRODUK", item.totalHarga)
                intent.putExtra("EXTRA_QTY", 1)

                context.startActivity(intent)

            }

            binding.btnHapusPesanan.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPesananListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Pesanan>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(pesanan: Pesanan) {
        val index = items.indexOfFirst { it.id == pesanan.id }
        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}