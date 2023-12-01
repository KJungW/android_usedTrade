package com.kjw.usetrade

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore

class ProductUpdateFragment: Fragment(R.layout.activity_productupdate) {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // activity의 뷰모델 가져오기
        val activity = requireActivity()
        viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val priceEdit = view.findViewById<EditText>(R.id.productUpdate_price_input)
        val isSoldRadio = view.findViewById<RadioButton>(R.id.productUpdate_isSold)
        val isSoldOutRadio = view.findViewById<RadioButton>(R.id.productUpdate_isSoldOut)
        val updateBt = view.findViewById<Button>(R.id.productUpdate_update)

        // 선택된 제품 id에 해당하는 docuemnt를 가져온다.
        val db = FirebaseFirestore.getInstance()
        val selectedDoc = db.collection("items").document(viewModel.getSelecedProductId())

        // 수정페이지의 초깃값 지정
        selectedDoc.get().addOnSuccessListener {
            priceEdit.setText(it.get("price").toString())
            if(it.get("isSold").toString().toBoolean())
                isSoldRadio.isChecked = true
            else
                isSoldOutRadio.isChecked = true
        }

        updateBt.setOnClickListener {
            it.isEnabled = false
            if(!priceEdit.text.trim().isNullOrBlank()) {
                selectedDoc.update("price", Integer.parseInt(priceEdit.text.toString()))
                if(isSoldRadio.isChecked)
                    selectedDoc.update("isSold", true)
                else
                    selectedDoc.update("isSold", false)

                findNavController().navigate(R.id.action_productUpdateFrag_to_productListFrag)
            } else{
                it.isEnabled = false
                Toast.makeText(view.context, "비어있는 곳 없이 입력해주세요", Toast.LENGTH_SHORT).show()
            }


        }

    }
}
