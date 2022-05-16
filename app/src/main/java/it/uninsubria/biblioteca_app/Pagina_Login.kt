package it.uninsubria.biblioteca_app

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import it.uninsubria.biblioteca_app.databinding.AppLoginBinding


class Pagina_Login : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: AppLoginBinding
    //ActionBar
    private lateinit var actionBar: ActionBar
    //ProcessDialog
    private lateinit var processDialog: ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    //Email e Password
    private var email = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AppLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //Configure process Dialog
        processDialog = ProgressDialog(this)
        processDialog.setTitle("Attendi...")
        processDialog.setMessage("Sto Entrando...")
        processDialog.setCanceledOnTouchOutside(false)

        //Inizializzazione firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //Apertura pagina registrazione
        binding.Registrati.setOnClickListener {
            startActivity(Intent(this,Pagina_Registrazione::class.java))
        }

        //Login
        binding.Accedi.setOnClickListener {
            //Prima del login
            validateData()
        }
        //Recupero password
        binding.RecuperoPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Inserisci l'Email che vuoi effettuare il reset")
            val view = layoutInflater.inflate(R.layout.finestra_dialogo_recupero_password, null)
            val email_recupero = view.findViewById<EditText>(R.id.et_email)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _ , _ ->
                forgotPassword(email_recupero)
            })
            builder.setNegativeButton("Close",DialogInterface.OnClickListener { _, _ ->  })
            builder.show()
        }

    }

    private fun forgotPassword(email_recupero: EditText) {
        if(email_recupero.text.toString().isEmpty()){
            //Inserire l'email
                Toast.makeText(this,"Inserire l'Email",Toast.LENGTH_SHORT).show()
                return
            }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_recupero.text.toString()).matches()){
                Toast.makeText(this,"Formato email non valido!",Toast.LENGTH_SHORT).show()
                return
        }
        firebaseAuth.sendPasswordResetEmail(email_recupero.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Email inviata!",Toast.LENGTH_SHORT).show()
                }

            }

    }

    private fun validateData() {
        //Prendi i dati
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //Controllo dati
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //Email non valida
            binding.emailEt.error = "Formato Email non valido!"
        }
        else if(TextUtils.isEmpty(password)){
            //Nessuna password immessa
            binding.passwordEt.error = "Inserisci la Password"
        }
        else{
            //Dati in ingresso sono corretti
            firebaseLogin()
        }


    }

    private fun firebaseLogin() {
        //Mostra progress
        processDialog.show()
        //Login con admin
        if ((email.equals("Admin@gmail.com")) && (password.equals("Admin"))) {
            startActivity(Intent(this, Pagina_Admin::class.java))
            finish()
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {

                    //Login Effettuato
                    processDialog.dismiss()
                    //Prendi Utente
                    val firebaseUser = firebaseAuth.currentUser
                    val email = firebaseUser!!.email
                    Toast.makeText(this, "Login da $email", Toast.LENGTH_SHORT).show()

                    //Apre la Homepage
                    startActivity(Intent(this, HomePage::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    //Login Saltato
                    processDialog.dismiss()
                    Toast.makeText(
                        this,
                        "login Fallito per causa di ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }

    private fun checkUser() {
        //Se l'utente ha già effettuato l'accesso, vai all'attività del profilo
        //Prendi l'utente corrente
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //Utente collegato.
            startActivity(Intent(this,HomePage::class.java))
            finish()

        }
    }
}