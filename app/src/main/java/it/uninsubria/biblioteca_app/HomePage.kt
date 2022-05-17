package it.uninsubria.biblioteca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.uninsubria.biblioteca_app.databinding.ActivityHomePageBinding
import it.uninsubria.biblioteca_app.Pagina_Admin
import java.net.URI
import android.net.Uri as Uri1


class HomePage : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityHomePageBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FireBaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    //RealTime Database Firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef:DatabaseReference


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

        //Configurazione Firebase RealTime Database Film
        database= FirebaseDatabase.getInstance()
        myRef=database.getReference("Film")




        //Pulsante di uscita
        binding.Esci.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        //Azione con Film
        binding.film.setOnClickListener {
            //visualizzaFilm()
            var intent=Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://movieplayer.it/film/ultime-uscite/"))
            startActivity(intent)


            Toast.makeText(this,"Hai selezionato Film",Toast.LENGTH_SHORT).show()
        }

        //Azione con Libri
        binding.libri.setOnClickListener {
            var intent= Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.mondadori.it/ultimi-libri-usciti/"))
            startActivity(intent)
            Toast.makeText(this,"Hai selezionato Libri",Toast.LENGTH_SHORT).show()
        }

        //Azione Musica
        binding.musica.setOnClickListener {
            var intent=(Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://www.earone.it/radio_date/")))
            startActivity(intent)
            Toast.makeText(this,"Hai selezionato Musica",Toast.LENGTH_SHORT).show()
        }

        //Azione Percorso Lettura
        binding.percorsoLettura.setOnClickListener {
            Toast.makeText(this,"Hai selezionato percorso di lettura",Toast.LENGTH_SHORT).show()
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