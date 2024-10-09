package sv.edu.udb.learnfacil

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sv.edu.udb.learnfacil.model.Recurso

class RecursoAdapter(
    private val context: Context,
    private val listaRecursos: List<Recurso>
) : RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = listaRecursos[position]
        holder.bind(recurso)

        holder.buttonEditarRecurso.setOnClickListener {
            val intent = Intent(context, ActualizarRecurso::class.java)
            intent.putExtra("RECURSO_ID", recurso.id)
            context.startActivity(intent)
        }

        holder.buttonEliminarRecurso.setOnClickListener {
            eliminarRecurso(recurso.id)
        }
    }

    override fun getItemCount(): Int {
        return listaRecursos.size
    }

    inner class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloRecurso: TextView = itemView.findViewById(R.id.tituloRecurso)
        val descripcionRecurso: TextView = itemView.findViewById(R.id.descripcionRecurso)
        val tipoRecurso: TextView = itemView.findViewById(R.id.tipoRecurso)
        val enlaceRecurso: TextView = itemView.findViewById(R.id.enlaceRecurso)
        val imagenRecurso: ImageView = itemView.findViewById(R.id.imagenRecurso)
        val buttonEditarRecurso: Button = itemView.findViewById(R.id.buttonEditarRecurso)
        val buttonEliminarRecurso: Button = itemView.findViewById(R.id.buttonEliminarRecurso)

        fun bind(recurso: Recurso) {
            tituloRecurso.text = recurso.titulo
            descripcionRecurso.text = recurso.descripcion
            tipoRecurso.text = recurso.tipo
            enlaceRecurso.text = recurso.enlace

            Glide.with(context)
                .load(recurso.imagen)
                .into(imagenRecurso)
        }


    }

    private fun eliminarRecurso(id: Int) {
        (context as MainActivity).eliminarRecurso(id)
    }
}

