package com.ssafy.smartcafe.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.dto.OrderDTOwithTotal
import com.ssafy.smartcafe.dto.ProductDTO

private const val TAG="ThisWeekTop3ListAdapter"
class ThisWeekTop3ListAdapter(var context: Context, private val resource: Int, list: List<OrderDTOwithTotal>) : RecyclerView.Adapter<ThisWeekTop3Holder>() {
    //사용하고자 하는 데이터
    private var top3List = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThisWeekTop3Holder {
        val view = LayoutInflater.from(parent.context).inflate(resource,parent,false)
        return ThisWeekTop3Holder(view)
    }

    override fun getItemCount(): Int {
        return top3List.size
    }

    override fun onBindViewHolder(holder: ThisWeekTop3Holder, position: Int) {

        var img = top3List[position].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productImg.setImageResource(resId)

        holder.productName.text = "${top3List[position].name}"


//        holder.orderDate.text = curInfo[0].order_time.substring(0,10)

//        holder.itemView.setOnClickListener {
//            //최근 주문 클릭하면
//            //tmp_list에 상품 담기게 하자
//            //상품 담은 뒤에 shoppingListActivity로 넘어가게 한다.
//            //println("????? : ${listData[position].id}")
//            Log.d(TAG, "onBindViewHolder: ${curInfo}")
//
//            for(i in curInfo.indices){
//                val orderProduct = OrderDetailDTO(-1,curInfo[i].p_id,Integer.parseInt(curInfo[i].quantity.toString()))
//
//                //data class OrderDetailForShoppingList(var img:String,var name: String, var quantity:Int,var price:Int,
//                //                       var totalprice: Int,var productId: Int)
//                var newOrderP = OrderDetailForShoppingList(
//                    curInfo[i].img, curInfo[i].name, curInfo[i].quantity, curInfo[i].price,
//                    curInfo[i].price*curInfo[i].quantity, curInfo[i].p_id, curInfo[i].o_id
//                )
//
//                tmpOrderList2.add(newOrderP)
//            }
//
//            var intent = Intent(this.context, ShoppingListActivity::class.java)
//            context.startActivity(intent)
//        }
    }


}

class ThisWeekTop3Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var productImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productName: TextView = itemView!!.findViewById(R.id.tv_item_name)
}