package it.uninsubria.biblioteca_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import it.uninsubria.biblioteca_app.databinding.ActivityImieiOrdiniBinding
import it.uninsubria.biblioteca_app.databinding.ActivityLibrilistBinding

import java.nio.file.Files.find

class Activity_lista_prestiti : AppCompatActivity() {

    private lateinit var myRef: DatabaseReference
    private lateinit var libri_in_prestito_RecycleView: RecyclerView
    private lateinit var libriArrayList: ArrayList<Database_libri>
    private lateinit var binding_libri_prestito: ActivityLibrilistBinding




    //FireBaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding_libri_prestito = ActivityLibrilistBinding.inflate(layoutInflater)
        setContentView(binding_libri_prestito.root)

        //Configurazione Firebaseauth
        firebaseAuth = FirebaseAuth.getInstance()

        libri_in_prestito_RecycleView = findViewById(R.id.librilist)
        libri_in_prestito_RecycleView.layoutManager = LinearLayoutManager(this)
        libri_in_prestito_RecycleView.setHasFixedSize(true)

        libriArrayList = arrayListOf<Database_libri>()
        prendiUtenti()




    }




    private fun prendiUtenti() {
        myRef = FirebaseDatabase.getInstance().getReference("Libri")

        //Controlla che l'utente sia connesso
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //Utente collegato
            val email = firebaseUser?.email
            System.out.println(email)
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {

                    for (i in p0.children) {
                        var email_utente = i.child("possibile_prenotazione").getValue()
                        System.out.println(email_utente)

                        if (email_utente.toString().equals(email)) {
                            val myLibri =i.getValue(Database_libri::class.java)
                            libriArrayList.add(myLibri!!)
                        }
                        libri_in_prestito_RecycleView.adapter =
                            I_Miei_Ordini(libriArrayList)

                            }
                        }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}


