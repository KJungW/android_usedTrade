package com.kjw.usetrade

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProductAddFragment : Fragment(R.layout.activity_productadd){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTitle = view.findViewById<EditText>(R.id.productadd_input_title)
        val editPrice = view.findViewById<EditText>(R.id.productadd_input_price)
        val editContent = view.findViewById<EditText>(R.id.productadd_input_content)

        view.findViewById<Button>(R.id.productadd_save)?.setOnClickListener {
            val thisBtn = it
            thisBtn.isEnabled = false
            val title = editTitle.text.toString().trim()
            val price = editPrice.text.toString().trim()
            val content = editContent.text.toString().trim()

            if(!(title.equals("") || price.equals("") || content.equals(""))) {
                val db: FirebaseFirestore = Firebase.firestore
                val collectionRef = db.collection("items")
                val itemMap = hashMapOf(
                    "title" to title,
                    "price" to Integer.parseInt(price),
                    "isSold" to true,
                    "content" to content,
                    "sender" to Firebase.auth.currentUser?.email
                )

                collectionRef.add(itemMap).addOnSuccessListener {
                    findNavController().navigate(R.id.action_productAddFrag_to_productListFrag)
                }.addOnFailureListener {
                    thisBtn.isEnabled = true
                }
            } else{
                Toast.makeText(view.context, "비어있는 곳 없이 입력해주세요", Toast.LENGTH_SHORT).show()
                thisBtn.isEnabled = true
            }

        }

    }
}
