package it.uninsubria.biblioteca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import it.uninsubria.biblioteca_app.databinding.ActivityHomePageBinding
import it.uninsubria.biblioteca_app.databinding.AppLoginBinding

class HomePage : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityHomePageBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FireBaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configurazione ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "HomePage"

        //Configurazione Firebaseauth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //Pulsante di uscita
        binding.Esci.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

    }

    private fun checkUser() {
        //Controlla che l'utente sia connesso
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //Utente collegato
            val email = firebaseUser.email
            //set to text view
            binding.emailTv.text = "Ciao ${email}"

        }else{
            //Utente non collegato
            startActivity(Intent(this,Pagina_Login::class.java))
            finish()
        }
    }
}