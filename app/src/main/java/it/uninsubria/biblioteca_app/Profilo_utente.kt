package it.uninsubria.biblioteca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import it.uninsubria.biblioteca_app.databinding.ActivityProfiloUtenteBinding



class Profilo_utente : AppCompatActivity() {
    //Binding
    private lateinit var binding_utente : ActivityProfiloUtenteBinding
    //Configurazione Firebase RealTime Database Film
    private lateinit var myRef: DatabaseReference
    //FireBaseAuth
    private lateinit var firebaseAuth : FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        myRef = FirebaseDatabase.getInstance().getReference("Utenti")
        super.onCreate(savedInstanceState)
        binding_utente = ActivityProfiloUtenteBinding.inflate(layoutInflater)
        setContentView(binding_utente.root)
        firebaseAuth = FirebaseAuth.getInstance()
        cerca_email()
        binding_utente.cancellaAccount.setOnClickListener {
            cancellaUtente()
        }
    }

    private fun cancellaUtente() {
        val firebaseUser = firebaseAuth.currentUser
        val email = firebaseUser?.email.toString()
        var leggiDati = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children) {
                    var nome_utente = i.child("nome").getValue()
                    var data_utente = i.child("data").getValue()
                    var username_utente = i.child("username").getValue()
                    var email_utente = i.child("email").getValue()

                    if (email_utente.toString().equals(email)) {
                        myRef = FirebaseDatabase.getInstance().getReference("Utenti")
                        firebaseUser?.delete()
                        myRef.child(nome_utente.toString()).removeValue().addOnSuccessListener {
                            Toast.makeText(this@Profilo_utente,"Utente eliminato correttamente",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Profilo_utente, Pagina_Login::class.java))
                        }.addOnFailureListener {
                            Toast.makeText(this@Profilo_utente,"Eliminazione fallita!",Toast.LENGTH_SHORT).show()
                        }


                    }

                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        myRef.addValueEventListener(leggiDati)
        myRef.addListenerForSingleValueEvent(leggiDati)

    }

    private fun cerca_email() {
        val firebaseUser = firebaseAuth.currentUser
        val email = firebaseUser?.email.toString()
        var leggiDati = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                for (i in p0.children) {
                    var nome_utente = i.child("nome").getValue()
                    var data_utente = i.child("data").getValue()
                    var username_utente = i.child("username").getValue()
                    var email_utente = i.child("email").getValue()

                    if (email_utente.toString().equals(email)) {
                        binding_utente.username.setText(username_utente.toString())
                        binding_utente.email.setText(email_utente.toString())
                        binding_utente.nome.setText(nome_utente.toString())
                        binding_utente.data.setText(data_utente.toString())


                    }

                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        }
        myRef.addValueEventListener(leggiDati)
        myRef.addListenerForSingleValueEvent(leggiDati)
    }




    }








