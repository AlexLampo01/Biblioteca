package it.uninsubria.biblioteca_app

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import it.uninsubria.biblioteca_app.databinding.ActivityPaginaPercorsiLetturaBinding
import it.uninsubria.biblioteca_app.databinding.ActivitySchermataPercorsiLetturaBinding


class Pagina_Percorsi_Lettura : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivityPaginaPercorsiLetturaBinding
    private lateinit var binding_percorsi: ActivitySchermataPercorsiLetturaBinding

    private lateinit var percorsi_lettura_RecycleView: RecyclerView
    private lateinit var percorsiArrayList: ArrayList<Database_libri>



    //RealTime Database Firebase

    private lateinit var myRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaginaPercorsiLetturaBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.cercaPercorso.setOnClickListener {

             binding_percorsi = ActivitySchermataPercorsiLetturaBinding.inflate(layoutInflater)
             setContentView(binding_percorsi.root)


             percorsi_lettura_RecycleView = findViewById(R.id.percorsilist)!!
             percorsi_lettura_RecycleView.layoutManager = LinearLayoutManager(this)
             percorsi_lettura_RecycleView.setHasFixedSize(true)
             percorsiArrayList = arrayListOf<Database_libri>()
            cercaPercorso()
        }
    }

    private fun cercaPercorso() {



        myRef = FirebaseDatabase.getInstance().getReference("Libri")


            myRef.addValueEventListener(object : ValueEventListener {


                override fun onDataChange(p0: DataSnapshot) {

                    val genere: String = binding.tipologiaRicercaEt.text.toString()
                    val sottogenere: String = binding.sottoGenereEt.text.toString()

                    for (i in p0.children) {
                        System.out.println("Sono nel For")
                        var Genere_percorsi = i.child("tipologia").getValue()
                        var Sottogenere_percorsi = i.child("sottoGenere").getValue()
                        System.out.println(Genere_percorsi)
                        System.out.println(Sottogenere_percorsi)

                        if ((Genere_percorsi.toString().equals(genere))&&(Sottogenere_percorsi.toString().equals(sottogenere))) {
                            System.out.println("Sono nell'if")
                            val myPercorso =i.getValue(Database_libri::class.java)

                           percorsiArrayList.add(myPercorso!!)
                            System.out.println("Parte nuova")
                        }
                        percorsi_lettura_RecycleView.adapter = Percorsi_Lettura(percorsiArrayList)

                    }
                    }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



    }
}

