package com.example.app_titulacion.utils

import android.os.AsyncTask
import android.util.Log
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class MailJob(
    private val user: String,
    private val pass: String) :
    AsyncTask<Mail?, Void?, Void?>() {

    override fun doInBackground(vararg params: Mail?): Void? {

        val props = Properties()

        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.host", "smtp.gmail.com")
        props.put("mail.smtp.port", "587")

        val session: Session = Session.getInstance(props,
            object : Authenticator() {
                override fun getPasswordAuthentication(): javax.mail.PasswordAuthentication {
                    return PasswordAuthentication(user, pass)
                }
            })

        val myArray = arrayListOf<Mail>()
         myArray.add(Mail(from = "walksafenotification@gmail.com", content = "description", subject = "Subjetct", to = "enriquerafaelbecerrabocangel@gmail.com"))
//         myArray.add(Mail(from = "enriquerafaelbecerrabocangel@gmail.com", content = "description", subject = "Subjetct", to = "enriquerafaelbecerrabocangel@gmail.com"))
//         myArray.add(Mail(from = "de mi pe 3 ", content = "description", subject = "Subjetct", to = "para ti@"))
//         myArray.add(Mail(from = "de mi pe 4", content = "description", subject = "Subjetct", to = "para ti@"))
//        = listOf(mail.from, mail.to, mail.subject,mail.content)

        for (mail in myArray) {
            try {
                val message: Message = MimeMessage(session)
                message.setFrom(InternetAddress(mail.from))
                message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mail.to)
                )
                message.setSubject(mail.subject)
                message.setText(mail.content)
                Transport.send(message)
            } catch (e: MessagingException) {
//                Log.d("MailJob", e.getMessage())
                Log.d("MailJob", e.message.toString())
            }
        }
        return null
    }
}


data class Mail(
    val from: String? = "",
    val to: String? = "",
    val subject: String? = "",
    val content: String? = ""
)
