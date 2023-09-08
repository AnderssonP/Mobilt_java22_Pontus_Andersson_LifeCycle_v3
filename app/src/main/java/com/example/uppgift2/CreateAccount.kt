package com.example.uppgift2

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseAuth
import java.security.KeyStore.PasswordProtection

class CreateAccount : Fragment() {

lateinit var exitButton: Button
lateinit var emailCreate: EditText
lateinit var passwordCreate: EditText
lateinit var buttonCreate: Button
private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_create_account, container, false)
            exitButton = view.findViewById(R.id.exitButton)
            emailCreate = view.findViewById(R.id.emailCreate)
            passwordCreate = view.findViewById(R.id.passwordCreate)
            buttonCreate = view.findViewById(R.id.buttonCreate)

            firebaseAuth = FirebaseAuth.getInstance();

            exitButton.setOnClickListener(View.OnClickListener {
                val transaction = fragmentManager?.beginTransaction()
                fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                val mainActivity = Intent(activity, MainActivity::class.java)
                startActivity(mainActivity)

                transaction?.commit()
            })

            buttonCreate.setOnClickListener(View.OnClickListener {
                val enterEmail = emailCreate.text.toString()
                val enterpassword = passwordCreate.text.toString()

                if (enterEmail.isNotEmpty() && enterpassword.isNotEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(enterEmail,enterpassword).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(activity,"Konto är skapat",Toast.LENGTH_LONG).show()
                            val intent = Intent(activity,MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(activity,"Finns redan",Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(activity,"Fyll i alla fält",Toast.LENGTH_LONG).show()
                }
            })
            return view
        }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateAccount().apply {
                arguments = Bundle().apply {
                }
            }
    }
}