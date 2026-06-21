package com.app.lokalmarket.v1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.v1.data.model.Kategori
import com.app.lokalmarket.databinding.ItemKategoriBinding

class KategoriCardAdapter(
    private val items: MutableList<Kategori>,
    private val onItemClick: (Kategori) -> Unit
) : RecyclerView.Adapter<KategoriCardAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemKategoriBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Kategori) {
            binding.tvKategoriName.text = item.namaKategori

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKategoriBinding.inflate(
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

    fun updateData(newItems: List<Kategori>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}