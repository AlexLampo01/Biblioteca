package it.uninsubria.biblioteca_app


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class I_Miei_Ordini(private val libri_in_prestito: ArrayList<Database_libri>, private val itemClickListener: itemClickListener): RecyclerView.Adapter<I_Miei_Ordini.ViewHolder>()  {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_imiei_ordini, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = libri_in_prestito[position]

        holder.itemNome.text = currentItem.nome
        holder.itemScrittore.text = currentItem.scrittore
        holder.itemTipologia.text = currentItem.tipologia
        holder.itemData.text = currentItem.data
        holder.itemRendi.setOnClickListener {
            itemClickListener.rendi(libri_in_prestito[position], position)
        }

    }

    override fun getItemCount(): Int {
            return libri_in_prestito.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemNome: TextView = itemView.findViewById(R.id.nome_libro_utente)
        var itemData: TextView = itemView.findViewById(R.id.data_libro_utente)
        var itemTipologia: TextView = itemView.findViewById(R.id.tipologia_libro_utente)
        var itemScrittore: TextView = itemView.findViewById(R.id.scrittore_libro_utente)
        var itemRendi: Button = itemView.findViewById(R.id.rendi_libro)

    }





}
