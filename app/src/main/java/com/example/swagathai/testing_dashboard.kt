package com.example.swagathai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.swagathai.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class testing_dashboard : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val auth by lazy {
        FirebaseAuth.getInstance()}
    lateinit var logout:Button
    lateinit var textViewWelcome:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testing_dashboard)
        //
        logout=findViewById(R.id.logout)
        textViewWelcome=findViewById(R.id.textViewWelcome)
        //
        val intent = intent
        val receivedEmail = intent.getStringExtra("emailAddress")
        textViewWelcome.text="Welcome "+receivedEmail

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id2))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
        logout.setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent= Intent(this, login_account::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}