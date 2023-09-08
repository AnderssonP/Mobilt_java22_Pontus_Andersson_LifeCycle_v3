package com.example.uppgift2


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UpdateInfo : Fragment() {
    lateinit var exitButton: Button
    lateinit var updateButton: Button
    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var age: EditText
    lateinit var phone: EditText
    lateinit var frog: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        exitButton = view.findViewById(R.id.exitButton2)
        updateButton = view.findViewById(R.id.fUpdate)
        name = view.findViewById(R.id.addName)
        email = view.findViewById(R.id.addEmail)
        age = view.findViewById(R.id.addAge)
        phone = view.findViewById(R.id.addPhone)
        frog = view.findViewById(R.id.addFrog)

        val db = Firebase.firestore

        updateButton.setOnClickListener(View.OnClickListener {
            val user = hashMapOf(
                "name" to name.text.toString(),
                "email" to email.text.toString(),
                "age" to age.text.toString(),
                "phone" to phone.text.toString(),
                "frog" to frog.text.toString()
            )

            val userEmail = email.text.toString()

            db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        val userId = document.id
                        db.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG, "Användaruppgifter uppdaterades framgångsrikt")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Fel vid uppdatering av användaruppgifter", e)
                            }
                    } else {
                        db.collection("users")
                            .add(user)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "Användare lades till med ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Fel vid tillägg av användare", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Fel vid hämtning av användarinformation", e)
                }
        })

        exitButton.setOnClickListener(View.OnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            fragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            val mainActivity = Intent(activity, MyAccount::class.java)
            startActivity(mainActivity)

            transaction?.commit()
        })


        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            UpdateInfo().apply {
                arguments = Bundle().apply {
                }
            }
    }
}