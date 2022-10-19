package com.example.swagathai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.swagathai.R
import com.example.swagathai.SavedPreference
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class login_account : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var txtforgot:TextView
    lateinit var sign_in_button:SignInButton
    lateinit var btnSignIn:Button
    lateinit var txtcreate:TextView
    lateinit var etemail:EditText
    lateinit var etpassword:EditText
    lateinit var textresponse:TextView
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_account)
        //
        auth = Firebase.auth
        sign_in_button=findViewById(R.id.sign_in_button)
        txtforgot=findViewById(R.id.txtforgot)
        btnSignIn=findViewById(R.id.btnSignIn)
        etemail=findViewById(R.id.etemail)
        etpassword=findViewById(R.id.etpassword)
        txtcreate=findViewById(R.id.txtcreate)
        textresponse=findViewById(R.id.textresponse)
        //
        btnSignIn.setOnClickListener{
            if (etemail.text.toString().isEmpty() || etpassword.text.toString().isEmpty()){
                textresponse.text = "Email Address or Password is not provided"
            }
            else {
                val inputMethodManager =  getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                auth.signInWithEmailAndPassword(etemail.text.toString(),etpassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user, etemail.text.toString() )
                        } else{
                            showCustomAlert()}
                    }}
            }

        txtforgot.setOnClickListener{
            val intent = Intent(this@login_account, forgot_password::class.java)
            startActivity(intent)
        }

        txtcreate.setOnClickListener{
            val intent = Intent(this@login_account, create_account::class.java)
            startActivity(intent)
        }

        //
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id2))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener { view: View? ->
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }
    }


    private fun showCustomAlert() {
        val dialogView = layoutInflater.inflate(R.layout.warning_message_sign_in, null)
        val customDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .show()
        customDialog.window?.setLayout(850,900)
        val btDismiss = dialogView.findViewById<ImageView>(R.id.imageView)
        val btDismiss2 = dialogView.findViewById<Button>(R.id.btntryagain)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
        }
        btDismiss2.setOnClickListener {
            val intent=Intent(this,create_account::class.java)
            startActivity(intent)
        }}

    private fun updateUI(currentUser: FirebaseUser?, emailAdd: String) {
        if(currentUser !=null){
            val intent = Intent(this, testing_dashboard::class.java)
            intent.putExtra("emailAddress", emailAdd);
            startActivity(intent)
            finish()
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