package it.uninsubria.biblioteca_app

import android.widget.Button
import android.widget.TextView

interface itemClickListener {
    fun rendi(databaseLibri: Database_libri, position : Int)

}