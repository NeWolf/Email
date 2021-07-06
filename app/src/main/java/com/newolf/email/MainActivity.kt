package com.newolf.email

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.newolf.maillib.MailIntentService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListener()
    }

    companion object{
        private const val ACC = ""
        private const val PWD = ""
    }

    private fun initListener() {
        findViewById<Button>(R.id.btn_send).setOnClickListener {
            val address = findViewById<EditText>(R.id.et_email).text.toString()
            val subject = findViewById<EditText>(R.id.et_subject).text.toString()
            val message =
                findViewById<EditText>(R.id.et_message).text.toString() + System.currentTimeMillis()

            MailIntentService.send(applicationContext, ACC, PWD, address, subject, message)
        }
    }
}