package com.example.swagathai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.swagathai.R

class forgot_password : AppCompatActivity() {
    lateinit var etemail:EditText
    lateinit var etnewpassword:EditText
    lateinit var etconfirm:EditText
    lateinit var btnreset:Button
    lateinit var backbutton:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)
        //
        etemail=findViewById(R.id.etemail)
        etnewpassword=findViewById(R.id.etnewpassword)
        etconfirm=findViewById(R.id.etconfirm)
        btnreset=findViewById(R.id.btnreset)
        backbutton=findViewById(R.id.backbutton)
        //
        btnreset.setOnClickListener{
            if (etemail.text.toString().isEmpty() ||
                etnewpassword.text.toString().isEmpty()||
                etconfirm.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    this@forgot_password,
                    "Fill details!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                val intent = Intent(this@forgot_password, login_account::class.java)
                startActivity(intent)
                Toast.makeText(
                    this,
                    "Credential updated successfully, now login",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
        backbutton.setOnClickListener{
            onBackPressed()
        }
    }
   }