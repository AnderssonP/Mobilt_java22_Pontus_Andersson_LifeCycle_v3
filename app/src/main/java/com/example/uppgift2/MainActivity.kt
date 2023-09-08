package com.example.uppgift2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var createAccount: Button
    private lateinit var loginButton: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var textName: TextView
    private lateinit var stay: CheckBox

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        createAccount = findViewById(R.id.createAccount)
        loginButton = findViewById(R.id.loginButton)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        textName = findViewById(R.id.textView2)
        stay = findViewById(R.id.checkBox)

        val storedEmail = sharedPreferences.getString("email", "")
        val storedPassword = sharedPreferences.getString("password", "")
        val stayLoggedIn = sharedPreferences.getBoolean("stayLoggedIn", false)

        if (stayLoggedIn) {
            email.setText(storedEmail)
            password.setText(storedPassword)
            stay.isChecked = true
        }

        createAccount.setOnClickListener(View.OnClickListener {
            val fm = supportFragmentManager
            fm.beginTransaction().add(R.id.CreateFragment, CreateAccount::class.java, null)
                .commit()
        })

        loginButton.setOnClickListener(View.OnClickListener {
            val enterEmail = email.text.toString()
            val enterPassword = password.text.toString()

            if (enterEmail.isNotEmpty() && enterPassword.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(enterEmail,enterPassword).addOnCompleteListener{
                    if (it.isSuccessful){
                        var user = firebaseAuth.currentUser?.email

                        val stayInside = sharedPreferences.edit()
                        stayInside.putString("email", enterEmail)
                        stayInside.putString("password", enterPassword)
                        stayInside.putBoolean("stayLoggedIn", stay.isChecked)
                        stayInside.apply()

                        val intent = Intent(this,MyAccount::class.java)
                        intent.putExtra("Name",user)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Konto finns inte", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(this,"Fyll i alla fält", Toast.LENGTH_LONG).show()
            }
        })
    }
}