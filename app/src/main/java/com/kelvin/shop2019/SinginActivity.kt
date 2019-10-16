package com.kelvin.shop2019

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_singin.*

class SinginActivity : AppCompatActivity() {
    private val RC_GOOGLE_SING_IN = 200
    private lateinit var googleSignInClient: GoogleSignInClient
    private  val TAG = SinginActivity::class.java.simpleName

    private val RC_TEST =500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singin)

        val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        google_sign_in.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent,RC_GOOGLE_SING_IN)
        }

        signUp.setOnClickListener {
            signUp()
        }
        login.setOnClickListener {
            login()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SING_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            Log.d(TAG,"onActivityResult: ${account?.id}")
            val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
            FirebaseAuth.getInstance()
                .signInWithCredential(credential)
                .addOnCompleteListener { task ->
                   if (task.isSuccessful){
                       setResult(Activity.RESULT_OK)
                       finish()
                   }else{
                       Log.d(TAG,"onActivityResult: ${task.exception?.message}")
                       Snackbar.make(main_signin, "Firebase authentication faild" , Snackbar.LENGTH_LONG).show()
                   }
                }

        }
    }

    private fun login() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setResult(Activity.RESULT_OK)
//                    startActivityForResult(
//                        Intent(this,TestActivity::class.java),
//                        RC_TEST  )
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
