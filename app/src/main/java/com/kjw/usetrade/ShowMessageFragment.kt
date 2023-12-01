package com.kjw.usetrade

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ShowMessageFragment : Fragment(R.layout.activity_showmessage) {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // activity의 뷰모델 가져오기
        val activity = requireActivity()
        viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleTv = view.findViewById<TextView>(R.id.showMessage_title)
        val fromTv = view.findViewById<TextView>(R.id.showMessage_from)
        val toTv = view.findViewById<TextView>(R.id.showMessage_to)
        val contentTv = view.findViewById<TextView>(R.id.showMessage_content)

        val db: FirebaseFirestore = Firebase.firestore
        val selectedDoc = db.collection("messages").document(viewModel.getSelectedMsgId())

        viewModel.setSelectedUser("")
        selectedDoc.get().addOnSuccessListener {
            titleTv.setText(it.get("title").toString())
            fromTv.setText("from : ${it.get("sender").toString()}")
            toTv.setText("to : ${it.get("receiver").toString()}")
            contentTv.setText(it.get("content").toString())
            viewModel.setSelectedUser(it.get("sender").toString())
        }

        view.findViewById<Button>(R.id.showMessage_send).setOnClickListener {
            if(!viewModel.getSelectedUser().equals(""))
                findNavController().navigate(R.id.action_showMessageFrag_to_sendMessageFrag)
            else
                Toast.makeText(view.context, "잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
        }
    }
}