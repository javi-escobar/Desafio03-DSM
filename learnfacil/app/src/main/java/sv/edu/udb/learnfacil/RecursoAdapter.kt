package sv.edu.udb.learnfacil

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sv.edu.udb.learnfacil.model.Recurso

class RecursoAdapter(private val context: Context, private val recursos: List<Recurso>) :
    RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    inner class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenRecurso: ImageView = itemView.findViewById(R.id.imagenRecurso)
        val tituloRecurso: TextView = itemView.findViewById(R.id.tituloRecurso)
        val descripcionRecurso: TextView = itemView.findViewById(R.id.descripcionRecurso)
        val tipoRecurso: TextView = itemView.findViewById(R.id.tipoRecurso)
        val enlaceRecurso: TextView = itemView.findViewById(R.id.enlaceRecurso)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]
        holder.tituloRecurso.text = recurso.titulo
        holder.descripcionRecurso.text = recurso.descripcion
        holder.tipoRecurso.text = recurso.tipo
        holder.enlaceRecurso.text = recurso.enlace

        Glide.with(context)
            .load(recurso.imagen)
            .into(holder.imagenRecurso)

        holder.enlaceRecurso.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(recurso.enlace)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return recursos.size
    }
}
