package it.uninsubria.biblioteca_app


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import it.uninsubria.biblioteca_app.databinding.ActivityHomePageBinding


class HomePage : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityHomePageBinding



    //ActionBar
    private lateinit var actionBar: ActionBar


    //FireBaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    //RealTime Database Firebase

    private lateinit var myRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        myRef = FirebaseDatabase.getInstance().getReference("Utenti")


        setContentView(binding.root)

        //Configurazione ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "HomePage"

        //Configurazione Firebaseauth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()






        //Azione con Film
        binding.film.setOnClickListener {
            //visualizzaFilm()
            val intent = Intent(
                Intent.ACTION_VIEW,
                android.net.Uri.parse("https://movieplayer.it/film/ultime-uscite/")
            )
            startActivity(intent)


            Toast.makeText(this, "Hai selezionato Film", Toast.LENGTH_SHORT).show()
        }

        //Azione con Libri
        binding.libri.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                android.net.Uri.parse("https://www.mondadori.it/ultimi-libri-usciti/")
            )
            startActivity(intent)
            Toast.makeText(this, "Hai selezionato Libri", Toast.LENGTH_SHORT).show()
        }

        //Azione Musica
        binding.musica.setOnClickListener {
            val intent = (Intent(
                Intent.ACTION_VIEW,
                android.net.Uri.parse("https://www.earone.it/radio_date/")
            ))
            startActivity(intent)
            Toast.makeText(this, "Hai selezionato Musica", Toast.LENGTH_SHORT).show()
        }

        //Azione Percorso Lettura
        binding.percorsoLettura.setOnClickListener {
            startActivity(Intent (this, Pagina_Percorsi_Lettura::class.java))
            Toast.makeText(this, "Hai selezionato percorso di lettura", Toast.LENGTH_SHORT).show()
        }

        //Cerca i libri nel database
        binding.cerca.setOnClickListener {
            val cerca_libri: String = binding.cercalibri.text.toString()
            if (cerca_libri.isEmpty()) {
                binding.cercalibri.error = "Inserisci il nome del libro"
            } else {

                LeggiLibri(cerca_libri)
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigazione_utente, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id == R.id.profilo){
            startActivity(Intent(this,Profilo_utente::class.java))
            return true
        }
        if(id == R.id.I_miei_Prestiti){
            startActivity(Intent(this,Activity_lista_prestiti::class.java))
            return true
        }
        if(id == R.id.logout){
            firebaseAuth.signOut()
            checkUser()
            return true
        }



        return super.onOptionsItemSelected(item)
    }


    private fun updateData(
        nome: String,
        possibile_prenotazione: String,

    ) {
        //Controlla che l'utente sia connesso


        val libri = mapOf<String, String>(
            "nome" to nome,
            "possibile_prenotazione" to possibile_prenotazione,
            "stato_prenotazione" to "Non Disponibile"
        )
        myRef.child(nome).updateChildren(libri).addOnSuccessListener {
            Toast.makeText(this, "Libro Prenotato", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this, "Prenotazione Fallita", Toast.LENGTH_SHORT).show()
        }

    }


    @SuppressLint("SetTextI18n")
    private fun LeggiLibri(cercaLibri: String) {
        myRef = FirebaseDatabase.getInstance().getReference("Libri")
        myRef.child(cercaLibri).get().addOnSuccessListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ecco la tua ricerca")
            val view = layoutInflater.inflate(R.layout.schermata_database, null)

            if (it.exists()) {
                val nome = it.child("nome").value
                val data = it.child("data").value
                val scrittore = it.child("scrittore").value
                val tipologia = it.child("tipologia").value
                val sottogenere = it.child("sottoGenere").value
                val prenotazione = it.child("stato_prenotazione").value
                val nome_libro = view.findViewById<TextView>(R.id.nome_libro)
                val data_libro = view.findViewById<TextView>(R.id.data_libro)
                val tipologia_libro = view.findViewById<TextView>(R.id.Tipologia_libro)
                val sottogenere_libro = view.findViewById<TextView>(R.id.SottoGenere_libro)
                val scrittore_libro = view.findViewById<TextView>(R.id.Scrittore_libro)

                nome_libro.setText("Nome del Libro: " + nome.toString())
                data_libro.setText("Data del libro:   "+data.toString())
                scrittore_libro.setText("Scrittore del libro:  "+scrittore.toString())
                tipologia_libro.setText("Genere del libro:   "+tipologia.toString())
                sottogenere_libro.setText("SottoGenere del libro:  "+sottogenere.toString())
                builder.setView(view)
                builder.setPositiveButton("Prenota Libro", { _, _ ->

                    val firebaseUser = firebaseAuth.currentUser
                    val email = firebaseUser?.email


                    if (prenotazione.toString() == "Disponibile") {
                        updateData(
                            nome as String,
                            email.toString(),

                        )
                    } else {
                        Toast.makeText(
                            this,
                            "Il libro che vuoi prenotare non è disponibile al momento!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                builder.show()

                Toast.makeText(this, "Il libro che hai cercato è presente", Toast.LENGTH_SHORT)
                    .show()


            } else {
                Toast.makeText(this, "Il libro cercato non è disponibile!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Ricerca fallita!", Toast.LENGTH_SHORT).show()
            }


    }


    private fun checkUser() {
        //Controlla che l'utente sia connesso
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //Utente collegato
            val email = firebaseUser.email
            //Permette di vedere Username con cui si ha l'accesso
            val leggiDati = object : ValueEventListener {

                @SuppressLint("SetTextI18n")
                override fun onDataChange(p0: DataSnapshot) {

                    for (i in p0.children) {

                        val username_utente = i.child("username").getValue()
                        val email_utente = i.child("email").getValue()

                        if (email_utente.toString().equals(email)) {
                            binding.emailTv.text = "Ciao: "+username_utente.toString()



                        }

                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            }
            myRef.addValueEventListener(leggiDati)
            myRef.addListenerForSingleValueEvent(leggiDati)

        } else {
            //Utente non collegato
            startActivity(Intent(this, Pagina_Login::class.java))
            finish()
        }

    }
}
