package com.ssafy.smartcafe.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelLazy
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.MenuDetailActivity
import com.ssafy.smartcafe.databinding.ActivityJoinBinding
import com.ssafy.smartcafe.databinding.FragmentAllBinding
import com.ssafy.smartcafe.dto.ProductDTO
import com.ssafy.smartcafe.viewModel.AllFragmentViewModel

private const val TAG="AllFragmentrAdapter_싸피"
//전체메뉴 어댑터
class ProductsListAdapter(var context: Context, val resource: Int, list:List<ProductDTO>) : RecyclerView.Adapter<AllFragmentHolder>() {
    //사용하고자 하는 데이터
    var listData = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFragmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return AllFragmentHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: AllFragmentHolder, position: Int) {

        var img = listData[position].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productImg.setImageResource(resId)
        //메뉴 상세페이지로 넘어가기
        holder.productImg.setOnClickListener{
            var intent = Intent(context, MenuDetailActivity::class.java)
            intent.putExtra("productId", listData[position].id.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

//        viewModel.setName(listData[position].name)
//        viewModel.setPrice("${listData[position].price}원")
        holder.productName.text = listData[position].name
        holder.productPrice.text = "${listData[position].price}원"
    }
}

class AllFragmentHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var productImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productName: TextView = itemView!!.findViewById(R.id.tv_item_name)
    var productPrice: TextView = itemView!!.findViewById(R.id.tv_price)
}
