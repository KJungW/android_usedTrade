package com.kjw.usetrade

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 사용할 데이터 클래스
data class ProductData(val title: String, val price: Int, val isSold:Boolean, val productId:String, val solderEmail:String)

// RecyclerView의 Adapter 정의
class PradocutAdapter(private var dataList: MutableList<ProductData>)
    : RecyclerView.Adapter<PradocutAdapter.ViewHolder>() {

    // 클릭이벤트 인터페이스
    interface OnItemClickListener {
        fun onItemClick(position: Int, viewHolder: ViewHolder)
    }
    var itemClickListener: OnItemClickListener? = null

    // ViewHolder 정의
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 아이템 레이아웃의 뷰들과 ViewHolder 내부를 매핑
        val title = itemView.findViewById<TextView>(R.id.productItem_title)
        val price = itemView.findViewById<TextView>(R.id.productItem_price)
        val isSold = itemView.findViewById<TextView>(R.id.productItem_isSold)
        var productId = ""
        var solderEmail = ""

        init {
            itemView.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition, this)
            }
        }
    }

    // ViewHolder를 생성하고 초기화하기
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ViewHolder로 사용할 레이아웃을 객체로 만든다. (만들어진 객체는 parent 뷰그룹과 연결된다.)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.productlist_item, parent, false)
        // ViewHolder를 생성해서 리턴
        return ViewHolder(itemView)
    }

    // ViewHolder에 데이터 넣기
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repoData = dataList[position]
        // 아이템 뷰를 데이터로 업데이트
        holder.title.text = repoData.title
        holder.price.text = repoData.price.toString()
        if(repoData.isSold) {
            holder.isSold.text = "판매중"
        } else {
            holder.isSold.text = "판매완료"
        }
        holder.productId = repoData.productId
        holder.solderEmail = repoData.solderEmail
    }
    // 어뎁터와 연결된 데이터의 개수를 리턴
    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(newData: MutableList<ProductData>) {
        dataList = newData
        notifyDataSetChanged()
    }

}