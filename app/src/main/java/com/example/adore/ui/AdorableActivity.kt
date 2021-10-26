package com.example.adore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.adore.R
import com.example.adore.databinding.ActivityAdorableBinding

class AdorableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Adore)
        val binding = DataBindingUtil.setContentView<ActivityAdorableBinding>(this,
            R.layout.activity_adorable
        )

//        val toolbar: Toolbar = findViewById(R.id.toolBar)
//        setSupportActionBar(toolbar)
    }
}

//val intent = Intent(this, LoginActivity::class.java)
//binding.btnLogin.setOnClickListener {
//    startActivity(intent)
//}