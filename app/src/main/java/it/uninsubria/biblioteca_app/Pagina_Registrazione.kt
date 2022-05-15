package it.uninsubria.biblioteca_app

import androidx.appcompat.app.ActionBar
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import it.uninsubria.biblioteca_app.databinding.ActivityPaginaRegistrazioneBinding

class Pagina_Registrazione : AppCompatActivity() {
    //View Binding
    private lateinit var binding: ActivityPaginaRegistrazioneBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProcessDialog
    private lateinit var processDialog: ProgressDialog

    //FireBase Autenticazione
    private lateinit var firebaseAuth:FirebaseAuth
    //Email e Password
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaginaRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Configurazione Actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Registrazione"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //Configurazione Process Dialog
        processDialog = ProgressDialog(this)
        processDialog.setTitle("Attendi...")
        processDialog.setMessage("Creazione Account...")
        processDialog.setCanceledOnTouchOutside(false)

        //Init Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //Gestione click per la registrazione
        binding.Registrati.setOnClickListener {
            validateData()
        }

    }

    private fun validateData() {
        //Prendi il dato
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //Controllo Dati
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //Invalido input
            binding.emailEt.error = "Email non corretta"
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEt.error = "Inserisci la password!"
        }
        else if(password.length <6){
            binding.passwordEt.error = "La password deve avere almeno 6 caratteri"
        }
        else {
            firebaseRegistrazione()
        }

    }

    private fun firebaseRegistrazione() {
        //Mostra progressi
        processDialog.show()

        //Crea un account
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener{
                //Registrazione effettuata
                processDialog.dismiss()
                //Prendi l'utente corrente
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Utente creato con l'email: $email",Toast.LENGTH_SHORT).show()
                //Apre HomePage
                startActivity(Intent(this,HomePage::class.java))
                finish()

            }
            .addOnFailureListener { e->
                //Fallimento nella registrazione
                processDialog.dismiss()
                Toast.makeText(this, "Registrazione fallita! Per causa di ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()//Torna indietro alla pagina di Login, quando premi il bottone dell'actionbar
        return super.onSupportNavigateUp()
    }
}