package com.example.adore.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.adore.models.dataClasses.Stock

@BindingAdapter("productImage")
fun ImageView.setProductImage(imageUrl: String){
    imageUrl?.let {
        Glide.with(this).load(imageUrl).into(findViewById(this.id))
    }
}

//@BindingAdapter("productNameString")
//fun TextView.setProductNameString(item: Product?){
//    item?.let {
//        text = item.name
//    }
//}

//@BindingAdapter("productDescriptionString")
//fun TextView.setProductDescriptionString(item: Product?){
//    item?.let {
//        text = it.description
//    }
//}

@BindingAdapter("productPriceString")
fun TextView.setProductPriceString(price: Float){
    price?.let {
        val price = "Rs. $price"
        text = price
    }
}

@BindingAdapter("productSizeViewXS")
fun TextView.setProductSizeViewXS(stock: Stock){
    stock.apply {
        if(availableCount<1){
            visibility = View.INVISIBLE
        }
    }
}