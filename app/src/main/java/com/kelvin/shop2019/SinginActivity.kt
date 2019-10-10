package com.kelvin.shop2019

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_singin.*

class SinginActivity : AppCompatActivity() {

    private val RC_TEST =500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singin)
        signUp.setOnClickListener {
            signUp()
        }
        login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivityForResult(
                        Intent(this,TestActivity::class.java),
                        RC_TEST  )
                    finish()
                } else {
                    AlertDialog.Builder(this@SinginActivity)
                        .setTitle("Login")
                        .setMessage(task.exception?.message)
                        .setPositiveButton("ok", null)
                        .show()
                }
            }
    }

    private fun signUp() {
        val sEmail = email.text.toString()
        val sPasswd = password.text.toString()
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(sEmail, sPasswd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AlertDialog.Builder(this@SinginActivity)
                        .setTitle("Sign Up")
                        .setMessage("Account created")
                        .setPositiveButton("ok") { dialog, which ->
                            setResult(Activity.RESULT_OK)
                            finish()
                        }.show()
                } else {
                    AlertDialog.Builder(this@SinginActivity)
                        .setTitle("Sign Up")
                        .setMessage(task.exception?.message)
                        .setPositiveButton("ok", null)
                        .show()
                }
            }
    }
}
