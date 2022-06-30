package com.ssafy.smartcafe.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
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
class ProductsListAdapter(var context: Context, val resource: Int, list:List<ProductDTO>) : RecyclerView.Adapter<ProductsListAdapter.AllFragmentHolder>(),Filterable {
    //사용하고자 하는 데이터
    var listData = list
    var filteredProduct = ArrayList<ProductDTO>()
    var itemFilter = ItemFilter()

    init {
        filteredProduct.addAll(listData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllFragmentHolder {
        val view = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        Log.d(TAG, "onCreateViewHolder: 됨????")
        return AllFragmentHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredProduct.size
    }

    override fun onBindViewHolder(holder: AllFragmentHolder, position: Int) {


        var img = filteredProduct[position].img
        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productImg.setImageResource(resId)
        //메뉴 상세페이지로 넘어가기
        holder.productImg.setOnClickListener {
            var intent = Intent(context, MenuDetailActivity::class.java)
            intent.putExtra("productId", filteredProduct[position].id.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

//        viewModel.setName(listData[position].name)
//        viewModel.setPrice("${listData[position].price}원")
        holder.productName.text = filteredProduct[position].name
        holder.productPrice.text = "${filteredProduct[position].price}원"
    }

    override fun getFilter(): Filter {
        return itemFilter
    }

    inner class ItemFilter:Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterString = constraint.toString()
            val results = FilterResults()
            Log.d(TAG, "charSequence : $constraint")

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList: ArrayList<ProductDTO> = ArrayList<ProductDTO>()
            //공백제외 아무런 값이 없을 경우 -> 원본 배열
            if (filterString.trim { it <= ' ' }.isEmpty()) {
                results.values = listData
                results.count = listData.size

                Log.d(TAG, "performFiltering: 필터가 되는거니?")
                return results
                //공백제외 2글자 이인 경우 -> 이름으로만 검색
            } else {
                for (data in listData) {
                    if (data.name.contains(filterString)) {
                        filteredList.add(data)
                    }
                }
            }
            results.values = filteredList
            results.count = filteredList.size

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredProduct.clear()
            filteredProduct.addAll(results?.values as ArrayList<ProductDTO>)
            notifyDataSetChanged()
        }

    }

    class AllFragmentHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var productImg: ImageView = itemView!!.findViewById(R.id.img_item)
        var productName: TextView = itemView!!.findViewById(R.id.tv_item_name)
        var productPrice: TextView = itemView!!.findViewById(R.id.tv_price)
    }
}
