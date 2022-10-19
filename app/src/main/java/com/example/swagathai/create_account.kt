package com.example.swagathai

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.swagathai.R
import com.example.swagathai.SavedPreference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class create_account : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var sign_in_button2:SignInButton
    lateinit var etname:EditText
    lateinit var etemail:EditText
    lateinit var btnregister:Button
    lateinit var etcreatepassword:EditText
    lateinit var backbutton:ImageView
    lateinit var txtmessage:TextView
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        auth = Firebase.auth
        sign_in_button2=findViewById(R.id.sign_in_button2)
        etname=findViewById(R.id.etname)
        etemail=findViewById(R.id.etemail)
        etcreatepassword=findViewById(R.id.etcreatepassword)
        btnregister=findViewById(R.id.btnregister)
        backbutton=findViewById(R.id.backbutton)
        txtmessage=findViewById(R.id.txtmessage)

        backbutton.setOnClickListener{
            onBackPressed()
        }
        btnregister.setOnClickListener{
            if (etname.text.toString().isEmpty()||
                etemail.text.toString().isEmpty() ||
                etcreatepassword.text.toString().isEmpty()) {
                txtmessage.text="Email Address or Password is not provided"
            }
            else {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                auth.createUserWithEmailAndPassword(
                    etemail.text.toString(),
                    etcreatepassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user, etemail.text.toString() )
                            Toast.makeText(
                                this,
                                "Welcome",
                                Toast.LENGTH_SHORT).show()
                            val intent=Intent(this,testing_dashboard::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showCustomAlert()
                        }
                    }
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id2))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        sign_in_button2.setOnClickListener { view: View? ->
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }


    }
    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }}

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }}

    private fun showCustomAlert() {
        val dialogView = layoutInflater.inflate(R.layout.error_message2, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()
        customDialog.window?.setLayout(900,900)
        val btDismiss = dialogView.findViewById<ImageView>(R.id.imageView)
        val btDismiss2 = dialogView.findViewById<Button>(R.id.btntryagain)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
        }
        btDismiss2.setOnClickListener {
            customDialog.dismiss()
        }
    }
    private fun updateUI(currentUser: FirebaseUser?, emailAdd: String) {
        if(currentUser !=null){
            val intent = Intent(this, testing_dashboard::class.java)
            intent.putExtra("emailAddress", emailAdd);
            startActivity(intent)
            finish()
        }
    }

private fun UpdateUI(account: GoogleSignInAccount) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val intent = Intent(this, testing_dashboard::class.java)
            SavedPreference.setEmail(this, account.email.toString())
            SavedPreference.setUsername(this, account.displayName.toString())
            startActivity(intent)
            finish()
        } } }

override fun onStart() {
    super.onStart()
    if (GoogleSignIn.getLastSignedInAccount(this) != null) {
        startActivity(Intent(this, testing_dashboard::class.java))
        finish()
    }}
}
