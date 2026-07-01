package com.app.lokalmarket.v1.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.v1.data.model.Kategori
import com.app.lokalmarket.databinding.ItemKategoriBinding
import com.app.lokalmarket.R

class KategoriCardAdapter(
    private val items: MutableList<Kategori>,
    private val onItemClick: (Kategori) -> Unit
) : RecyclerView.Adapter<KategoriCardAdapter.ViewHolder>() {

    private var selectedPosition = 0
    inner class ViewHolder(
        private val binding: ItemKategoriBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Kategori, position: Int) {
            binding.tvKategoriName.text = item.namaKategori
            val context = binding.root.context

            if (position == selectedPosition) {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange_primary))
                binding.tvKategoriName.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                binding.tvKategoriName.setTextColor(ContextCompat.getColor(context, R.color.orange_primary))
            }

            binding.root.setOnClickListener {
                val previousPosition = selectedPosition

                selectedPosition = adapterPosition

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

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
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Kategori>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}