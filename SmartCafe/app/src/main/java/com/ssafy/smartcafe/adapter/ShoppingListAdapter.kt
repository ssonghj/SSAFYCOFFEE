package com.ssafy.smartcafe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity.Companion.detailList
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.dto.ShoppingListDTO

private const val TAG="ShoppingListAdapter"
class ShoppingListAdapter(var context: Context, private val resource: Int, var productList: MutableList<ShoppingListDTO>)
    : RecyclerView.Adapter<ShoppingListHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return ShoppingListHolder(view)
    }

    override fun getItemCount(): Int {
        return detailList.size
    }

    override fun onBindViewHolder(holder: ShoppingListHolder, position: Int) {

        var img = productList[position].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.image.setImageResource(resId)
        holder.productName.text = productList[position].name
        holder.quantity.text = productList[position].quantity.toString()
        holder.price.text = "${productList[position].price}원"
        holder.sumPrice.text = "${productList[position].sumPrice}원"

        holder.btn_minus.setOnClickListener{
            var quantity = holder.quantity.text.toString().toInt()-1
            if(quantity<1){
                quantity = 1
            }

            detailList[position].quantity = quantity

            productList[position].quantity = quantity
            productList[position].sumPrice = quantity * productList[position].price

            holder.quantity.text = quantity.toString()
            holder.sumPrice.text = "${productList[position].sumPrice}원"

            notifyItemChanged(position)
        }

        holder.btn_plus.setOnClickListener{
            var quantity = holder.quantity.text.toString().toInt()+1

            detailList[position].quantity = quantity

            productList[position].quantity = quantity
            productList[position].sumPrice = quantity * productList[position].price

            holder.quantity.text = quantity.toString()
            holder.sumPrice.text = "${productList[position].sumPrice}원"
            notifyItemChanged(position)
        }

        //삭제시
        holder.btn_delete.setOnClickListener{
            productList.removeAt(position)
            detailList.removeAt(position)
            notifyDataSetChanged()
        }
    }

}


class ShoppingListHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var image :ImageView = itemView!!.findViewById(R.id.img_item)
    var productName: TextView = itemView!!.findViewById(R.id.tv_item_name)
    var quantity: TextView = itemView!!.findViewById(R.id.tv_quantity)
    var price: TextView = itemView!!.findViewById(R.id.tv_single_price)
    var sumPrice: TextView = itemView!!.findViewById(R.id.tv_total_price)

    var btn_plus: TextView = itemView!!.findViewById(R.id.btn_plus)
    var btn_minus: TextView = itemView!!.findViewById(R.id.btn_minus)

    var btn_delete: ImageView = itemView!!.findViewById(R.id.btn_delete)
}