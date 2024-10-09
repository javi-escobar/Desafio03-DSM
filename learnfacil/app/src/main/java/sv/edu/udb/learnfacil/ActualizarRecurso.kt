package sv.edu.udb.learnfacil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sv.edu.udb.learnfacil.model.Recurso

class ActualizarRecurso : AppCompatActivity() {

    private lateinit var api: ApiService
    private lateinit var editTextTitulo: EditText
    private lateinit var editTextDescripcion: EditText
    private lateinit var editTextTipo: EditText
    private lateinit var editTextEnlace: EditText
    private lateinit var editTextImagen: EditText
    private lateinit var buttonGuardar: Button
    private var recursoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_recurso)

        recursoId = intent.getIntExtra("RECURSO_ID", -1)

        if (recursoId == -1) {
            Toast.makeText(this, "ID de recurso inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://6705a449031fd46a8310a102.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)

        editTextTitulo = findViewById(R.id.editTextTitulo)
        editTextDescripcion = findViewById(R.id.editTextDescripcion)
        editTextTipo = findViewById(R.id.editTextTipo)
        editTextEnlace = findViewById(R.id.editTextEnlace)
        editTextImagen = findViewById(R.id.editTextImagen)
        buttonGuardar = findViewById(R.id.buttonGuardar)

        obtenerDatosRecurso(recursoId)

        buttonGuardar.setOnClickListener {
            actualizarRecurso()
        }
    }

    private fun obtenerDatosRecurso(id: Int) {
        val call = api.obtenerRecursosPorId(id)
        call.enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    val recurso = response.body()
                    recurso?.let {
                        editTextTitulo.setText(it.titulo)
                        editTextDescripcion.setText(it.descripcion)
                        editTextTipo.setText(it.tipo)
                        editTextEnlace.setText(it.enlace)
                        editTextImagen.setText(it.imagen)
                    } ?: run {
                        Toast.makeText(this@ActualizarRecurso, "Recurso no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ActualizarRecurso, "Error al obtener recurso: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@ActualizarRecurso, "Error en la llamada: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarRecurso() {
        val titulo = editTextTitulo.text.toString()
        val descripcion = editTextDescripcion.text.toString()
        val tipo = editTextTipo.text.toString()
        val enlace = editTextEnlace.text.toString()
        val imagen = editTextImagen.text.toString()

        if (titulo.isNotEmpty() && descripcion.isNotEmpty() && tipo.isNotEmpty() && enlace.isNotEmpty() && imagen.isNotEmpty()) {
            val recursoActualizado = Recurso(recursoId, titulo, descripcion, tipo, enlace, imagen)

            val call = api.actualizarRecurso(recursoId, recursoActualizado)
            call.enqueue(object : Callback<Recurso> {
                override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ActualizarRecurso, "Recurso actualizado correctamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ActualizarRecurso, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@ActualizarRecurso, "Error al actualizar: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Recurso>, t: Throwable) {
                    Toast.makeText(this@ActualizarRecurso, "Error en la llamada: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
        }
    }
}

