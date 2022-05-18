package it.uninsubria.biblioteca_app

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import it.uninsubria.biblioteca_app.databinding.ActivityPaginaAdminBinding
import it.uninsubria.biblioteca_app.databinding.ActivityPaginaRegistrazioneBinding
import java.net.URI

class Pagina_Admin : AppCompatActivity() {
    //View Binding
    private lateinit var binding: ActivityPaginaAdminBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProcessDialog
    private lateinit var processDialog: ProgressDialog

    //Database RealTime Firebase
    private lateinit var database:FirebaseDatabase
    private lateinit var myRef:DatabaseReference




    private var nome = ""
    private var data = ""
    private var tipologia = ""
    private var casa_produttrice = ""
    private var stato_prenotazione = ""

    val ImageBack = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaginaAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configurazione Actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Registrazione"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //Configurazione Process Dialog
        processDialog = ProgressDialog(this)
        processDialog.setTitle("Attendi...")
        processDialog.setMessage("Salvataggio in corso...")
        processDialog.setCanceledOnTouchOutside(false)


        binding.Registra.setOnClickListener {
            insierisciDati()
        }

    }





    private fun insierisciDati() {
        nome = binding.NomeEt.text.toString().trim()
        data = binding.DataEt.text.toString().trim()
        tipologia = binding.TipologiaEt.text.toString().trim()
        casa_produttrice = binding.ScrittoreEt.text.toString().trim()
        stato_prenotazione = "Disponibile"
        //Configurazione RealTime Database Firebase per Film

        //Configurazione RealTime Database Firebase per Libri
                    database = FirebaseDatabase.getInstance("https://biblioteca-70e70-default-rtdb.firebaseio.com/")
            myRef = database.getReference("Libri")

        if(TextUtils.isEmpty(nome)){
            //Nessun Nome immesso
            binding.NomeEt.error = "Inserisci Nome"
        }
        else if(TextUtils.isEmpty(data)){
            //Nessuna data immessa
            binding.DataEt.error = "Inserisci la Data"
        }
        else if(TextUtils.isEmpty(tipologia)){
            //Nessuna tipologia immessa
            binding.TipologiaEt.error = "Inserisci la Tipologia"
        }
        else if(TextUtils.isEmpty(casa_produttrice)){
            //Nessuna Casa immessa
            binding.ScrittoreEt.error = "Inserisci la Casa"
        }

        else{

            var model = Database_Inserimento(nome,data,tipologia,casa_produttrice,stato_prenotazione)

            var id = myRef.push().key
             //Invio dati
            myRef.child(nome!!).setValue(model)
            Toast.makeText(this,"Dati aggiunti al database",Toast.LENGTH_SHORT).show()

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()//Torna indietro alla pagina di Login, quando premi il bottone dell'actionbar
        return super.onSupportNavigateUp()
    }
}