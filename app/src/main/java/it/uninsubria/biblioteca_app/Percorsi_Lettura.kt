package it.uninsubria.biblioteca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Percorsi_Lettura(private val percorsi_Lettura: ArrayList<Database_libri>): RecyclerView.Adapter<Percorsi_Lettura.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.schermata_percorsi, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = percorsi_Lettura[position]

        holder.itemNome.text = currentItem.nome
        holder.itemScrittore.text = currentItem.scrittore
        holder.itemTipologia.text = currentItem.tipologia
        holder.itemData.text = currentItem.data


    }

    override fun getItemCount(): Int {
        return percorsi_Lettura.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemNome: TextView = itemView.findViewById(R.id.nome_libro_utente)
        var itemData: TextView = itemView.findViewById(R.id.data_libro_utente)
        var itemTipologia: TextView = itemView.findViewById(R.id.tipologia_libro_utente)
        var itemScrittore: TextView = itemView.findViewById(R.id.scrittore_libro_utente)
    }
}