package com.newolf.maillib

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * MailIntentService
 *
 * @author NeWolf
 * @since 2021-07-06
 */
open class MailIntentService : IntentService(TAG) {
    companion object {
        const val TAG = "MailIntentService"
        private const val STR_EXTRA_Q_ACC = "str_extra_q_acc"
        private const val STR_EXTRA_Q_PWD = "str_extra_q_pwd"
        private const val STR_EXTRA_EMAIL = "str_extra_email"
        private const val STR_EXTRA_SUBJECT = "str_extra_subject"
        private const val STR_EXTRA_MSG = "str_extra_msg"
        private const val FROM_SEND = "from_send"

        fun send(context: Context, account: String, pwd: String, email: String, subject: String, msg: String) {
            val intent = Intent(context, MailIntentService::class.java)
            intent.putExtra(STR_EXTRA_Q_ACC, account)
                    .putExtra(STR_EXTRA_Q_PWD, pwd)
                    .putExtra(STR_EXTRA_EMAIL, email)
                    .putExtra(STR_EXTRA_SUBJECT, subject)
                    .putExtra(STR_EXTRA_MSG, msg)
                    .putExtra(FROM_SEND, FROM_SEND)
            context.startService(intent)
        }
    }

    private lateinit var session: Session

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        if (!TextUtils.equals(FROM_SEND, intent.getStringExtra(FROM_SEND))) {
            Log.e(TAG, "onHandleIntent: not from send ,return ,please use send")
            return
        }

        Log.e(TAG, "onHandleIntent: start send" )

        val acc = intent.getStringExtra(STR_EXTRA_Q_ACC)
        val pwd = intent.getStringExtra(STR_EXTRA_Q_PWD)
        val email = intent.getStringExtra(STR_EXTRA_EMAIL)
        val subject = intent.getStringExtra(STR_EXTRA_SUBJECT)
        val msg = intent.getStringExtra(STR_EXTRA_MSG)

        val properties = Properties()
        properties.put("mail.smtp.host", "smtp.qq.com")
        properties.put("mail.smtp.socketFactory.port", "465")
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        properties.put("mail.smtp.auth", "true")
        properties.put("mail.smtp.port", "465")

        session = Session.getDefaultInstance(properties, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(acc, pwd)
            }
        })

        val mimeMessage = MimeMessage(session)
        try {
            mimeMessage.setFrom(InternetAddress(acc))
            mimeMessage.addRecipients(Message.RecipientType.TO, java.lang.String.valueOf(InternetAddress(email)))
            mimeMessage.subject = subject
            mimeMessage.setText(msg)
            Transport.send(mimeMessage)
            Log.e(TAG, "onHandleIntent: send sus")
        } catch (e: Exception) {
            Log.e(TAG, "onHandleIntent: send fail by exception ", e)
        }
    }


}