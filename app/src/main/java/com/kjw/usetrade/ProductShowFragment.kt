package com.kjw.usetrade

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProductShowFragment : Fragment(R.layout.activity_productshow) {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // activity의 뷰모델 가져오기
        val activity = requireActivity()
        viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTv = view.findViewById<TextView>(R.id.productShow_title)
        val priceTv = view.findViewById<TextView>(R.id.productShow_price)
        val isSoldTv = view.findViewById<TextView>(R.id.productShow_isSold)
        val solderNameTv = view.findViewById<TextView>(R.id.productShow_solder_name)
        val solderEmailTv = view.findViewById<TextView>(R.id.productShow_solder_email)
        val contentTv = view.findViewById<TextView>(R.id.productShow_content)
        val sendMsgBt = view.findViewById<Button>(R.id.productShow_chat)


        // 선택된 제품 id에 해당하는 docuemnt를 가져온다.
        val db = FirebaseFirestore.getInstance()
        val selectedDoc = db.collection("items").document(viewModel.getSelecedProductId())


        // 가져온 document를 통해 제품상세페이지를 채운다.
        selectedDoc.get().addOnSuccessListener {
            titleTv.setText("제품명: ${it.get("title").toString()}")
            priceTv.setText("가격: ${it.get("price").toString()}")
            if(it.get("isSold").toString().toBoolean())
                isSoldTv.setText("판매여부: 판매중")
            else
                isSoldTv.setText("판매여부: 판매완료")

            db.collection("users").whereEqualTo("email", it.get("sender").toString()).get().addOnSuccessListener {
                solderNameTv.setText("판매자: ${it.documents[0].get("name").toString()}")
            }

            solderEmailTv.setText("판매자메일: \n ${it.get("sender").toString()}")
            contentTv.setText("상품설명: \n ${it.get("content").toString()}")

            if(it.get("sender").toString().equals(Firebase.auth.currentUser?.email)) {
                sendMsgBt.isVisible = false
            }
        }

        // 메세지 보내기 버튼의 클릭 처리함수
        sendMsgBt.setOnClickListener {
            selectedDoc.get().addOnSuccessListener {
                viewModel.setSelectedUser(it.get("sender").toString())
                findNavController().navigate(R.id.action_productShowFrag_to_sendMessageFrag)
            }
        }
    }
}