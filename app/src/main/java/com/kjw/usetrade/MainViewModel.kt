package com.kjw.usetrade

import androidx.lifecycle.ViewModel

public class MainViewModel : ViewModel() {
    // 현재 선택된 제품 id를 저장하는 뷰모델
    private var selectedProductId : String = ""
    
    // 현재 선택된 메세지 id를 저장하는 뷰모델
    private var selectedMsgId : String = ""

    private var selectedUser : String = ""

    // selecedProductId set함수
    public fun setSelectedProductId(productId:String) {
        selectedProductId = productId
    }

    // selecedProductId get함수
    public fun getSelecedProductId() :String{
        return selectedProductId
    }

    public fun setSelectedMsgId(msgId:String){
        selectedMsgId = msgId
    }

    public fun getSelectedMsgId():String{
        return selectedMsgId
    }

    public fun setSelectedUser(userEmail:String){
        selectedUser = userEmail
    }

    public fun getSelectedUser():String{
        return selectedUser
    }
}