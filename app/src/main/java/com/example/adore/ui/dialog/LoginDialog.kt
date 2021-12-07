package com.example.adore.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatDialog
import com.example.adore.databinding.DialogLoginBinding
import com.example.adore.ui.dialogListener.LoginDialogListener
import com.google.android.material.textfield.TextInputLayout

class LoginDialog(context: Context, var listener: LoginDialogListener) : AppCompatDialog(context) {
    lateinit var binding:DialogLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogLoginBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener{
                val mobileNo = loginFtilEtMobileNo.editText?.text.toString()
                val password = loginFtilEtPassword.editText?.text.toString()

                if(validateAllFields()){
                    listener.onLoginButtonClicked(mobileNo, password)
                    dismiss()
                }
            }

            tvClickHere.setOnClickListener {
                listener.onCreateNewAccountClicked()
                dismiss()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    private fun validateAllFields():Boolean{
        var fields: List<TextInputLayout>
        binding.apply {
            fields = listOf(loginFtilEtMobileNo, loginFtilEtPassword)
        }
        return !fields.map { it.validateField() }.contains(false)
    }

    private fun TextInputLayout.validateField() = when {
        editText?.text.toString().isEmpty() -> {
            error = "Field cannot be empty"
            false
        }
        editText?.text.toString().length<5 ->{
            error = "Invalid values!"
            false
        }
        else -> {
            error = null
            true
        }
    }
}