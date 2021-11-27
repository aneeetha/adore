package com.example.adore.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.adore.databinding.DialogLoginBinding
import com.example.adore.ui.dialogListener.LoginDialogListener

class LoginDialog(context: Context, var listener: LoginDialogListener) : AppCompatDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DialogLoginBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener{
                val mobileNo = loginFtilEtMobileNo.editText?.text.toString()
                val password = loginFtilEtPassword.editText?.text.toString()

                if(mobileNo.isEmpty() || password.isEmpty()){
                    Toast.makeText(context, "Please fill all information!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                listener.onLoginButtonClicked(mobileNo, password)
                dismiss()
            }

            tvClickHere.setOnClickListener {
                listener.onCreateNewAccountClicked()
                dismiss()
            }
        }
    }
}