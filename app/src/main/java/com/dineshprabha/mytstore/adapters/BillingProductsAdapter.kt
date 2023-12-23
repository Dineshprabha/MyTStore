package com.dineshprabha.mytstore.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dineshprabha.mytstore.R
import com.dineshprabha.mytstore.data.Address
import com.dineshprabha.mytstore.data.Cartproduct
import com.dineshprabha.mytstore.databinding.BillingProductsRvItemBinding
import com.dineshprabha.mytstore.helper.getProductPrice

class BillingProductsAdapter: RecyclerView.Adapter<BillingProductsAdapter.BillingViewHolder>() {

    inner class BillingViewHolder(val binding : BillingProductsRvItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(billingProduct: Cartproduct) {
            binding.apply {
                Glide.with(itemView).load(billingProduct.product.images[0]).into(imageCartProduct)
                tvProductCartName.text = billingProduct.product.name
                tvBillingProductQuantity.text = billingProduct.quantity.toString()

                val priceAfterPercentage = billingProduct.product.offerPercentage.getProductPrice(billingProduct.product.price)
                tvProductCartPrice.text = "Rs. ${String.format("%.2f", priceAfterPercentage)}"

                imageCartProductColor.setImageDrawable(ColorDrawable(billingProduct.selectedColor?: Color.TRANSPARENT))
                tvCartProductSize.text = billingProduct.selectedSize?:"".also { imageCartProductSize.setImageDrawable(ColorDrawable(
                    Color.TRANSPARENT)) }
            }

        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Cartproduct>(){
        override fun areItemsTheSame(oldItem: Cartproduct, newItem: Cartproduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: Cartproduct, newItem: Cartproduct): Boolean {
            return oldItem == newItem
        }


    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        return BillingViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val billingProduct  = differ.currentList[position]
        holder.bind(billingProduct)


    }

    var onClick : ((Address) -> Unit)? = null
}