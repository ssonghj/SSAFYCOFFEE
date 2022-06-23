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
import com.ssafy.smartcafe.activity.ShoppingListActivity
import com.ssafy.smartcafe.dto.RecentOrderDTO
import java.util.LinkedHashMap

private const val TAG="RecentOrderListAdapter"
class RecentOrderListAdapter(var context: Context, private val resource: Int,
                             objects: LinkedHashMap<Int, MutableList<RecentOrderDTO>>,
                             var mapKey:MutableList<Int>)
    : RecyclerView.Adapter<HomeHolder>() {

    //사용하고자 하는 데이터
    private var orderList = objects

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(resource, parent, false)

        return HomeHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        //한번에 orderId별로 다 가져와서 두개 이상이면 ~외 ~잔, 이름, 가격 , 날짜 출력
        var curInfo = orderList.getValue(mapKey[position])

        var img = curInfo[0].img

        var resId = context.resources.getIdentifier(
            img.substring(0, img.length - 4),
            "drawable",
            "com.ssafy.smartcafe"
        )
        holder.productsImg.setImageResource(resId)


        var sum = 0
        var totalQuantity = 0

        Log.d(TAG, "onBindViewHolder: ${curInfo}")
        if(curInfo.size >= 2){
            for(i in curInfo.indices){
                sum += curInfo[i].price * curInfo[i].quantity
                totalQuantity += curInfo[i].quantity
            }
            holder.productsName.text = "${curInfo[0].name} 외 ${totalQuantity}잔"
        }else{
            sum += curInfo[0].price * curInfo[0].quantity
            holder.productsName.text = "${curInfo[0].name}"
        }


        holder.orderPrice.text = "${sum}원"


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

    override fun getItemCount(): Int {
        return orderList.size
    }
}

class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var productsImg: ImageView = itemView!!.findViewById(R.id.img_item)
    var productsName: TextView = itemView!!.findViewById(R.id.tv_item_name)
    var orderPrice: TextView = itemView!!.findViewById(R.id.tv_item_price)
//    var orderDate: TextView = itemView!!.findViewById(R.id.tv_item_orderDate)

}