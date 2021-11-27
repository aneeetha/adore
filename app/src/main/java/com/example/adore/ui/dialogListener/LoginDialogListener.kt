package com.example.adore.ui.dialogListener

interface LoginDialogListener {
    fun onLoginButtonClicked(mobileNo: String, password: String)

    fun onCreateNewAccountClicked()
}