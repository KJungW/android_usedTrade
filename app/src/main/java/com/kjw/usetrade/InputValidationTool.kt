package com.kjw.usetrade

import android.util.Patterns
import android.widget.TextView

class InputValidationTool {

    companion object {
        // 비밀번호 유호성 검사를 위한 정규식 (8~15글자)
        val passwordPattern = "^.{8,15}$".toRegex()
        val birthPattern = "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])\$".toRegex()

        // 이메일 유효성 검사
        fun checkEmailValidation (inputEmail:String) : Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()
        }

        // 비밀번호 유효성 검사
        fun checkPwValidation (inputPw:String) : Boolean {
            return passwordPattern.matches(inputPw)
        }

        // 체크 비밀번호 검사
        fun checkPwCheckValidation (inputPw1:String, inputPw2:String) : Boolean {
            return inputPw1 == inputPw2
        }

        // 이름 유효성 검사
        fun checkNameValidation (inputName:String) :Boolean {
            return !inputName.trim().isEmpty()
        }

        // 생년월일 유효성 검사
        fun checkBirthValidation (inputBirth:String) :Boolean {
            return birthPattern.matches(inputBirth)
        }

        // 로그인 유효성 검사
        fun checkLoginValidation (email:String, pw:String, guideTv1:TextView, guideTv2:TextView) :Boolean {
            if(!checkEmailValidation(email)){
                loginValidationConditionPrint(guideTv1, guideTv2, validationCondition.emailX)
                return false
            }
            else if(!checkPwValidation(pw)){
                loginValidationConditionPrint(guideTv1, guideTv2, validationCondition.pwX)
                return false
            }
            else{
                loginValidationConditionPrint(guideTv1, guideTv2, validationCondition.ok)
                return true
            }
        }

        // 회원가입 유효성검사
        fun checkSignValidation (email:String, pw:String, pw2:String, name:String, birth:String, guideTv1:TextView, guideTv2:TextView) :Boolean {
            if(!checkEmailValidation(email)){
                signValidationConditionPrint(guideTv1, guideTv2, validationCondition.emailX)
                return false
            }
            else if(!checkPwValidation(pw)){
                signValidationConditionPrint(guideTv1, guideTv2, validationCondition.pwX)
                return false
            }
            else if(!checkPwCheckValidation(pw, pw2)){
                signValidationConditionPrint(guideTv1, guideTv2, validationCondition.pwCheckX)
                return false
            }
            else if(!checkNameValidation(name)) {
                signValidationConditionPrint(guideTv1, guideTv2, validationCondition.nameX)
                return false
            }
            else if(!checkBirthValidation(birth)){
                signValidationConditionPrint(guideTv1, guideTv2, validationCondition.birthX)
                return false
            }
            else{
                signValidationConditionPrint(guideTv1, guideTv2, validationCondition.ok)
                return true
            }
        }

        fun loginValidationConditionPrint (tv1:TextView, tv2: TextView, condition: validationCondition) {
            if(condition == validationCondition.emailX) {
                tv1.setText("잘못된 형식의 이메일 입력")
                tv2.setText("  ")
            } else if(condition == validationCondition.pwX) {
                
                tv1.setText("(8~15글자)")
                tv2.setText("잘못된 형식의 비밀번호 입력")
            } else {
                tv1.setText("  ")
                tv2.setText("  ")
            }
        }

        fun signValidationConditionPrint (tv1:TextView, tv2: TextView, condition: validationCondition) {
            if(condition == validationCondition.emailX) {
                tv1.setText("잘못된 형식의 이메일 입력")
                tv2.setText("  ")
            } else if(condition == validationCondition.pwX) {
                tv1.setText("(8~15글자)")
                tv2.setText("잘못된 형식의 비밀번호 입력")
            } else if(condition == validationCondition.pwCheckX) {
                tv1.setText("비밀번호가 일치하지 않습니다.")
                tv2.setText(" ")
            } else if(condition == validationCondition.nameX) {
                tv1.setText("이름을 입력해주세요")
                tv2.setText(" ")
            } else if(condition == validationCondition.birthX) {
                tv1.setText("YYMMDD형식의 생년월일을 입력해주세요")
                tv2.setText(" ")
            } else {
                tv1.setText("  ")
                tv2.setText("  ")
            }
        }
    }
}

enum class validationCondition{
    ok, emailX, pwX, pwCheckX, nameX, birthX
}