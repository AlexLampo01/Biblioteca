package it.uninsubria.biblioteca_app

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import it.uninsubria.biblioteca_app.databinding.ActivityPaginaAdminBinding
import it.uninsubria.biblioteca_app.databinding.ActivityPaginaRegistrazioneBinding

class Pagina_Admin : AppCompatActivity() {
    //View Binding
    private lateinit var binding: ActivityPaginaAdminBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProcessDialog
    private lateinit var processDialog: ProgressDialog
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
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()//Torna indietro alla pagina di Login, quando premi il bottone dell'actionbar
        return super.onSupportNavigateUp()
    }
}