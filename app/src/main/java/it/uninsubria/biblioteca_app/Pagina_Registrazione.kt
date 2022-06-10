package it.uninsubria.biblioteca_app

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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

    //Firebase RealTime Database
    private lateinit var database:FirebaseDatabase
    private lateinit var myRef: DatabaseReference
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
        //Impossibilità di registrare questa email
        if(email.equals("Admin@gmail.com")){
            binding.emailEt.error= "Non è possibile effettuare la registrazione con questa email!"
        }
        else {
            //Controllo Dati
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //Invalido input
                binding.emailEt.error = "Email non corretta"
            } else if (TextUtils.isEmpty(password)) {
                binding.passwordEt.error = "Inserisci la password!"
            } else if (password.length < 6) {
                binding.passwordEt.error = "La password deve avere almeno 6 caratteri"
            } else {
                inserimentofirebaserealtime()
                firebaseRegistrazione()
            }
        }
    }

    private fun inserimentofirebaserealtime() {
        //Inserimento dati utente nel database
         val nome = binding.nomeEt.text.toString().trim()
         val data = binding.dataEt.text.toString().trim()
         val email = binding.emailEt.text.toString().trim()
         val username = binding.usernameEt.text.toString().trim()

        database = FirebaseDatabase.getInstance("https://biblioteca-database-default-rtdb.firebaseio.com/")
        myRef = database.getReference("Utenti")

        if(TextUtils.isEmpty(nome)){
            //Nessun Nome immesso
            binding.nomeEt.error = "Inserisci Nome"
        }
        else if(TextUtils.isEmpty(data)){
            //Nessuna data immessa
            binding.dataEt.error = "Inserisci la Data"
        }
        else if(TextUtils.isEmpty(username)){
            //Nessun username immesso
            binding.usernameEt.error = "Inserisci Username"
        }
        else{

            var model = Database_Utenti(nome,data,username,email)

            var id = myRef.push().key
            //Invio dati
            myRef.child(nome!!).setValue(model)
            Toast.makeText(this,"Dati aggiunti al database",Toast.LENGTH_SHORT).show()

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