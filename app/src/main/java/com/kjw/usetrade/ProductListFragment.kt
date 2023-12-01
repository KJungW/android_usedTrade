package com.kjw.usetrade

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductListFrag :  Fragment(R.layout.activity_productlist){
    private lateinit var adapter : PradocutAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // activity의 뷰모델 가져오기
        val activity = requireActivity()
        viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 어뎁터 생성
        adapter = PradocutAdapter(mutableListOf())

        // recyclerView 가져오기
        val recyclerView = view.findViewById<RecyclerView>(R.id.productlist_recyclerview)

        // recyclerView의 크기고정
        recyclerView.setHasFixedSize(true)
        // recyclerView의 레이아웃매니저 등록 (현재는 LinearLayoutManager이지만 다른 레이아웃 매니저도 활요가능)
        recyclerView.layoutManager = LinearLayoutManager(getActivity())
        // recyclerView와 어뎁터 연결
        recyclerView.adapter = adapter

        getFullProductList()

        // 제품추가 버튼클릭 처리함수
        view.findViewById<Button>(R.id.productList_add)?.setOnClickListener {
            findNavController().navigate(R.id.action_productListFrag_to_productAddFrag)
        }

        // 정렬하기 버튼클릭 처리함수
        view.findViewById<Button>(R.id.productList_sort)?.setOnClickListener {
            showRadioButtonDialog(view)
        }

        view.findViewById<ImageButton>(R.id.productList_message)?.setOnClickListener {
            findNavController().navigate(R.id.action_productListFrag_to_messageListFrag)
        }

        // 리사이클러뷰 항목 클릭이벤트 등록
        adapter.itemClickListener = object : PradocutAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, holder: PradocutAdapter.ViewHolder) {
                viewModel.setSelectedProductId(holder.productId)
                if(holder.solderEmail.equals(Firebase.auth.currentUser?.email)) {
                    // 본인이 작성한 제품이면 수정페이지로 이동
                    findNavController().navigate(R.id.action_productListFrag_to_productUpdateFrag)
                } else {
                    // 다른 사람이 작성한 제품이면 제품상세페이지로 이동
                    findNavController().navigate(R.id.action_productListFrag_to_productShowFrag)
                }
            }
        }

    }

    // firestore에서 전체 제품리스트 데이터를 가져온다.
    fun getFullProductList() {
        val db: FirebaseFirestore = Firebase.firestore
        val collectionRef = db.collection("items")

        collectionRef.get().addOnSuccessListener {
            val items = mutableListOf<ProductData>()
            for (doc in it) {
                items.add(ProductData(doc["title"].toString(), Integer.parseInt(doc["price"].toString()),
                                      doc["isSold"].toString().toBoolean(), doc.id.toString(), doc["sender"].toString()))
            }
            adapter?.updateData(items)
        }
    }

    // firestore에서 판매여부에 따른 제품리스트 데이터를 가져온다.
    fun getProductListUsingIsSold(isSold:Boolean) {
        val db: FirebaseFirestore = Firebase.firestore
        val collectionRef = db.collection("items")

        collectionRef.whereEqualTo("isSold", isSold).get().addOnSuccessListener {
            val items = mutableListOf<ProductData>()
            for (doc in it) {
                items.add(ProductData(doc["title"].toString(), Integer.parseInt(doc["price"].toString()),
                    doc["isSold"].toString().toBoolean(), doc.id.toString(), doc["sender"].toString()))
            }
            adapter?.updateData(items)
        }
    }

    // 필터링 다이어로그를 띠우는 함수
    private fun showRadioButtonDialog(view: View) {
        // 다이얼로그 빌더생성
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("제품리스트 정렬")

        // 라디오 그룹 생성
        val radioGroup = RadioGroup(view.context)
        radioGroup.orientation = RadioGroup.VERTICAL

        // 다이얼로그에서 선택가능한 옵션배열
        val options = arrayOf("판매중인 제품으로 필터링", "판매완료된 제품으로 필터링", "전체 제품보기")

        // 옵션만큼 라디오버튼을 생성하고 라디오 그룹에 할당
        for (i in options.indices) {
            val radioButton = RadioButton(view.context)
            radioButton.text = options[i]
            radioButton.id = i
            radioGroup.addView(radioButton)
        }

        // 다이얼로그 빌더에 라디오 그룹등록
        builder.setView(radioGroup)
        
        // 확인 버튼 클릭 시 동작 정의
        builder.setPositiveButton("OK") { dialog, which ->
            when (radioGroup.checkedRadioButtonId) {
                0 -> getProductListUsingIsSold(true)
                1 -> getProductListUsingIsSold(false)
                2 -> getFullProductList()
            }
        }
        
        builder.create().show()
    }

}
