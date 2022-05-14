package it.uninsubria.biblioteca_app

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.app.ProgressDialog
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

    }

    private fun checkUser() {
        TODO("Not yet implemented")
    }
}