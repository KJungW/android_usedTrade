package com.kjw.usetrade

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MessageListFragment :  Fragment(R.layout.activity_messagelist) {
    private lateinit var adapter: MessageAdapter
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
        adapter = MessageAdapter(mutableListOf())
        // recyclerView 가져오기
        val recyclerView = view.findViewById<RecyclerView>(R.id.messageList_recyclerView)

        // recyclerView의 크기고정
        recyclerView.setHasFixedSize(true)
        // recyclerView의 레이아웃매니저 등록 (현재는 LinearLayoutManager이지만 다른 레이아웃 매니저도 활요가능)
        recyclerView.layoutManager = LinearLayoutManager(getActivity())
        // recyclerView와 어뎁터 연결
        recyclerView.adapter = adapter

        // 리사이클러뷰 항목 클릭이벤트 등록
        adapter.itemClickListener = object : MessageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, holder: MessageAdapter.ViewHolder) {
                if(!holder.msgId.equals(""))
                {
                    viewModel.setSelectedMsgId(holder.msgId)
                    findNavController().navigate(R.id.action_messageListFrag_to_showMessageFrag)
                }
            }
        }

        getFullMessageList()
    }

    // firestore에서 전체 메세지 리스트 데이터를 가져온다.
    fun getFullMessageList() {

        val db: FirebaseFirestore = Firebase.firestore
        val collectionRef = db.collection("messages")

        collectionRef.whereEqualTo("receiver", Firebase.auth.currentUser?.email)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val items = mutableListOf<MessageHeaderRecord>()
                for (doc in it) {
                    items.add(
                        MessageHeaderRecord(
                            doc.id, doc["title"].toString(),
                            doc["sender"].toString(), doc["receiver"].toString()
                        )
                    )
                }
                if(items.size > 0)
                    adapter?.updateData(items)
                else
                    adapter?.updateData(mutableListOf(MessageHeaderRecord("","메세지 없음","","")))
        }
    }
}
