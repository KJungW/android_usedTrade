package com.kjw.usetrade

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SendMessageFragment: Fragment(R.layout.activity_sendmessage) {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // activity의 뷰모델 가져오기
        val activity = requireActivity()
        viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전송버튼 클릭 처리
        view.findViewById<Button>(R.id.sendMsg_send).setOnClickListener {
            sendMessage(view)
        }
    }

    // 메세지를 DB에 저장하는 메서드
    fun sendMessage(view: View){
        val btn = view.findViewById<Button>(R.id.sendMsg_send)
        btn.isEnabled = false
        val title = view.findViewById<EditText>(R.id.sendMsg_title).text.toString().trim()
        val content = view.findViewById<EditText>(R.id.sendMsg_content).text.toString().trim()

        if(title.equals("") || content.equals("")){
            btn.isEnabled = true
            Toast.makeText(view.context, "비어있는 곳 없이 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        else{
            val db = FirebaseFirestore.getInstance()
            val itemMap = hashMapOf(
                "title" to title,
                "content" to content,
                "sender" to Firebase.auth.currentUser?.email,
                "receiver" to viewModel.getSelectedUser(),
                "timestamp" to Date()
            )

            val messsageRef = db.collection("messages")
            messsageRef.add(itemMap).addOnSuccessListener {
                findNavController().popBackStack()
            }.addOnFailureListener(){
                btn.isEnabled = true
            }
        }

    }
}

