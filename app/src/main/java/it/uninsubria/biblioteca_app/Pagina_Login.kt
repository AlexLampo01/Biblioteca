package it.uninsubria.biblioteca_app

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.app.ProgressDialog
import android.content.Intent
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