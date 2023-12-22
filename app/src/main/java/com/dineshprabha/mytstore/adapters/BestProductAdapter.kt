package com.dineshprabha.mytstore.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dineshprabha.mytstore.data.Product
import com.dineshprabha.mytstore.databinding.ProductRvItemBinding
import com.dineshprabha.mytstore.helper.getProductPrice

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductsViewHolder>(){

    inner class BestProductsViewHolder(private val binding: ProductRvItemBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(product: Product){

            binding.apply {

                Glide.with(itemView).load(product.images[0]).into(imgProduct)

                val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                tvNewPrice.text = "Rs. ${String.format("%.2f", priceAfterOffer)}"
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                if (product.offerPercentage == null)
                    tvNewPrice.visibility = View.INVISIBLE
                tvName.text = product.name
                tvPrice.text = product.price.toString()
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick :((Product) -> Unit)? = null
}