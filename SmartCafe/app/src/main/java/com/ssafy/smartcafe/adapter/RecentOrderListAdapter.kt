package com.ssafy.smartcafe.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartcafe.R
import com.ssafy.smartcafe.activity.LoginActivity
import com.ssafy.smartcafe.activity.ShoppingListActivity
import com.ssafy.smartcafe.dto.OrderDetailDTO
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

        //최근 주문 메뉴 장바구니에 넣고 토스트메시지 띄우기
        holder.productsImg.setOnClickListener{

            var curInfo = orderList.getValue(mapKey[position])
            for(i in curInfo.indices){

                var quantity = curInfo[i].quantity
                var productId = curInfo[i].p_id
                var details = OrderDetailDTO(0,0,productId,quantity)
                LoginActivity.detailList.add(details)
            }
            Toast.makeText(context, "최근 주문내역 장바구니에 담기 완료 !", Toast.LENGTH_SHORT).show()
        }

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