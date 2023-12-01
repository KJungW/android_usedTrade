package com.kjw.usetrade

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class StartFrag :  Fragment(R.layout.activity_start){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 로그인 버튼 클릭시 로그인 Fragment로 이동
        view.findViewById<Button>(R.id.strat_goLogin)?.setOnClickListener {
            findNavController().navigate(R.id.action_startFrag_to_loginFrag)
        }
    }
}

class LoginFrag :  Fragment(R.layout.activity_login){

    private lateinit var inputGuidline1 : TextView
    private lateinit var inputGuidline2 : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEdit = view.findViewById<EditText>(R.id.login_input_email)
        val pwEdit = view.findViewById<EditText>(R.id.login_input_pw)
        inputGuidline1 = view.findViewById<TextView>(R.id.login_msg1)
        inputGuidline2 = view.findViewById<TextView>(R.id.login_msg2)

        // 이메일을 입력할때마다 유효성 검사
        emailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("loginFrag", "이메일 입력 : ${s.toString()}")
                InputValidationTool.checkLoginValidation(s.toString(), pwEdit.text.toString(), inputGuidline1, inputGuidline2)
            }
            override fun afterTextChanged(s: Editable) {}
        })

        // 비밀번호를 입력할때마다 유효성 검사
        pwEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("loginFrag", "비밀번호 입력 : ${s.toString()}")
                InputValidationTool.checkLoginValidation(emailEdit.text.toString(), s.toString(), inputGuidline1, inputGuidline2)
            }
            override fun afterTextChanged(s: Editable) {}
        })

        // 로그인 버튼 클릭시 로그인 수행후 상품 Fragment로 이동
        view.findViewById<Button>(R.id.login_login)?.setOnClickListener {
            val email = emailEdit.text.toString()
            val pw = pwEdit.text.toString()
            // 로그인전 다시한번 유효성 검사
            val isValid = InputValidationTool.checkLoginValidation(email, pw, inputGuidline1, inputGuidline2)
            // 유효성 검사를 통과하면 삼품리스트페이지로 이동
            if(isValid) doLogin(email, pw, it)
        }
        
        // 회원가입 버튼 클릭시 회원가입 Fragment로 이동
        view.findViewById<Button>(R.id.login_singup)?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFrag_to_signupFrag)
        }
    }

    // 로그인 수행함수
    private fun doLogin(userEmail: String, password: String, btn: View) {
        btn.isEnabled = false

        // 로그인 수행 (Firebase.auth : firebase의 인증 API 제공 객체)
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener() {
                // 로그인 성공시 실행되는 코드
                if (it.isSuccessful) {
                    findNavController().navigate(R.id.action_loginFrag_to_productListFrag)
                } 
                // 로그인 실패시 실행되는 코드
                else{
                    inputGuidline1.setText("계정 정보가 없습니다")
                    btn.isEnabled = true
                }
            }
    }

}

class SignupFrag :  Fragment(R.layout.activity_signup) {

    private lateinit var inputGuidline1: TextView
    private lateinit var inputGuidline2: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEdit = view.findViewById<EditText>(R.id.signup_input_email)
        val pwEdit = view.findViewById<EditText>(R.id.signup_input_pw1)
        val pwCheckEdit = view.findViewById<EditText>(R.id.signup_input_pw2)
        val nameEdit = view.findViewById<EditText>(R.id.signup_input_name)
        val birthEdit = view.findViewById<EditText>(R.id.signup_input_birth)

        inputGuidline1 = view.findViewById<TextView>(R.id.signup_msg1)
        inputGuidline2 = view.findViewById<TextView>(R.id.signup_msg2)

        // 이메일을 입력할때마다 유효성 검사
        emailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("loginFrag", "이메일 입력 : ${s.toString()}")
                InputValidationTool.checkSignValidation(
                    s.toString(),
                    pwEdit.text.toString(),
                    pwCheckEdit.text.toString(),
                    nameEdit.text.toString(),
                    birthEdit.text.toString(),
                    inputGuidline1,
                    inputGuidline2
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // 비밀번호를 입력할때마다 유효성 검사
        pwEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("loginFrag", "비밀번호 입력 : ${s.toString()}")
                InputValidationTool.checkSignValidation(
                    emailEdit.text.toString(),
                    s.toString(),
                    pwCheckEdit.text.toString(),
                    nameEdit.text.toString(),
                    birthEdit.text.toString(),
                    inputGuidline1,
                    inputGuidline2
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        pwCheckEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("loginFrag", "비밀번호 체크 입력 : ${s.toString()}")
                InputValidationTool.checkSignValidation(
                    emailEdit.text.toString(),
                    pwEdit.text.toString(),
                    s.toString(),
                    nameEdit.text.toString(),
                    birthEdit.text.toString(),
                    inputGuidline1,
                    inputGuidline2
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("loginFrag", "이름 체크 입력 : ${s.toString()}")
                InputValidationTool.checkSignValidation(
                    emailEdit.text.toString(), pwEdit.text.toString(), pwCheckEdit.text.toString(),
                    s.toString(), birthEdit.text.toString(), inputGuidline1, inputGuidline2
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        birthEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("loginFrag", "생년월일 체크 입력 : ${s.toString()}")
                InputValidationTool.checkSignValidation(
                    emailEdit.text.toString(), pwEdit.text.toString(), pwCheckEdit.text.toString(),
                    nameEdit.text.toString(), s.toString(), inputGuidline1, inputGuidline2
                )
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // 회원가입 버튼 클릭시 상품 Fragment로 이동
        view.findViewById<Button>(R.id.signup_signup)?.setOnClickListener {
            val email = emailEdit.text.toString()
            val pw = pwEdit.text.toString()
            val pw2 = pwCheckEdit.text.toString()
            val name = nameEdit.text.toString()
            val birth = birthEdit.text.toString()
            // 회원가입전 다시한번 유효성 검사
            val isValid = InputValidationTool.checkSignValidation(
                email,
                pw,
                pw2,
                name,
                birth,
                inputGuidline1,
                inputGuidline2
            )
            // 유효성 검사를 통과하면 회원가입완료 페이지로 이동
            if (isValid) doSignup(email, pw, name, birth, it)
        }

    }

    // 회원가입 수행함수
    private fun doSignup(userEmail: String, password: String, name: String, birth: String, btn: View) {
        btn.isEnabled = false
        val db = Firebase.firestore
        val usersCollection = db.collection("users")

        var itemMap = hashMapOf(
            "email" to userEmail,
            "name" to name,
            "birth" to birth,
        )

        // 회원가입
        Firebase.auth.createUserWithEmailAndPassword(userEmail, password).addOnSuccessListener {
            Log.d("SignFrag", "회원가입 과정1 성공")
            // 로그인 수행
            Firebase.auth.signInWithEmailAndPassword(userEmail, password).addOnSuccessListener {
                Log.d("SignFrag", "회원가입 과정2 성공")
                // 부가 정보를 DB에 저장
                usersCollection.add(itemMap).addOnSuccessListener {
                    Log.d("SignFrag", "회원가입 과정3 성공")
                    findNavController().navigate(R.id.action_signupFrag_to_productListFrag)
                }.addOnFailureListener {
                    FirebaseAuth.getInstance().getCurrentUser()?.delete();
                    inputGuidline2.setText("회원가입 실패, 다시 시도해주세요")
                }
            }
        }.addOnFailureListener() {
            inputGuidline2.setText("회원가입 실패, 다시 시도해주세요")
            inputGuidline1.setText("(작성된 이메일에서 @뒤가 특수기호가 아닌지 확인)")
            btn.isEnabled = true
        }

    }
}
