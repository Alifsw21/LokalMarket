package com.app.lokalmarket.v1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lokalmarket.R
import com.app.lokalmarket.v1.data.model.Promo
import com.app.lokalmarket.databinding.ItemPromoCardBinding

class PromoCardAdapter(
    private val items: MutableList<Promo>
) : RecyclerView.Adapter<PromoCardAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemPromoCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(promo: Promo) {
            binding.tvJudulPromo.text = promo.judulPromo
            binding.tvDiskonPromo.text = "Diskon ${promo.diskon}%"

            if (promo.statusAktif != "Aktif") {
                binding.tvStatus.visibility = View.VISIBLE
                binding.tvStatus.text = promo.statusAktif
            } else {
                binding.tvStatus.visibility = View.GONE
            }

            val imageRes = when (promo.id) {
                1 -> R.drawable.promo1
                else -> R.drawable.ic_launcher_background
            }
            binding.ivPromoImage.setImageResource(imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPromoCardBinding.inflate(
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

    fun updateData(newItems: List<Promo>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}