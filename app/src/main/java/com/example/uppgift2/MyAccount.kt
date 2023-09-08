package com.example.uppgift2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.content.SharedPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyAccount : AppCompatActivity() {

    private lateinit var inloggName: TextView
    private lateinit var name: TextView
    private lateinit var picture: ImageView
    private lateinit var age: TextView
    private lateinit var email: TextView
    private lateinit var phone: TextView
    private lateinit var update: Button
    private lateinit var logout: Button
    private lateinit var froggen: TextView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        inloggName = findViewById(R.id.Name)
        name = findViewById(R.id.fName)
        froggen = findViewById(R.id.froggen)
        age = findViewById(R.id.fAge)
        email = findViewById(R.id.fEmail)
        phone = findViewById(R.id.fPhone)
        update = findViewById(R.id.updateButton)
        logout = findViewById(R.id.logout)

        var accountName = intent.getStringExtra("Name")

        if (accountName.isNullOrEmpty()) {
            accountName = sharedPreferences.getString("accountName", "")
        } else {
            saveAccountName(accountName)
        }
        inloggName.text = "Välkommen $accountName"
        email.text = "E-mail: $accountName"

        val userEmail = sharedPreferences.getString("email", "")
        if (!userEmail.isNullOrEmpty()) {
            fetchUserInfo(userEmail)
        }

        logout.setOnClickListener(View.OnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        })

        update.setOnClickListener(View.OnClickListener {
            val fm = supportFragmentManager
            fm.beginTransaction().add(R.id.updateFragment, UpdateInfo::class.java, null)
                .commit()
        })
    }

    private fun fetchUserInfo(email: String) {
        val db = Firebase.firestore
        val usersRef = db.collection("users")

        usersRef.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val nameGet = document.getString("name")
                    val ageGet = document.getString("age")
                    val phoneNumber = document.getString("phone")
                    val frogInfo = document.getString("frog")

                    name.text= "Namn: $nameGet"
                    age.text = "Ålder: $ageGet"
                    phone.text = "Telefonnummer: $phoneNumber"
                    froggen.text = "Min favoit groda:  $frogInfo"
                } else {
                }
            }
            .addOnFailureListener { exception ->
            }
    }


    fun saveAccountName(accountName: String) {
        val editor = sharedPreferences.edit()
        editor.putString("accountName", accountName)
        editor.apply()
    }

}