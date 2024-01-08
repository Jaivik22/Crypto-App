package com.example.cryptoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CryptoAdapter(private val cryptoList: List<CurrencyDetails>) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullNameTextView: TextView = itemView.findViewById(R.id.fullNameTextView)
        val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        val rateTextView: TextView = itemView.findViewById(R.id.rateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_crypto, parent, false)
        return CryptoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val currentItem = cryptoList[position]
        holder.fullNameTextView.text = currentItem.fullName

        // it makes exchange rate, rounded-off to 6 decimal places.
        val formattedRate = String.format("%.6f", currentItem.rate ?: 0.0)
        holder.rateTextView.text = "$formattedRate"

        //helps to load image in imageView
        Picasso.get().load(currentItem.iconUrl).into(holder.iconImageView)

    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }
}
