package sv.edu.udb.learnfacil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sv.edu.udb.learnfacil.model.Recurso

class AgregarRecurso : AppCompatActivity() {
    private lateinit var editTextTitulo: EditText
    private lateinit var editTextDescripcion: EditText
    private lateinit var editTextTipo: EditText
    private lateinit var editTextEnlace: EditText
    private lateinit var editTextImagen: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_recurso)

        editTextTitulo = findViewById(R.id.editTextTitulo)
        editTextDescripcion = findViewById(R.id.editTextDescripcion)
        editTextTipo = findViewById(R.id.editTextTipo)
        editTextEnlace = findViewById(R.id.editTextEnlace)
        editTextImagen = findViewById(R.id.editTextImagen)
        val buttonGuardar: Button = findViewById(R.id.buttonGuardar)

        buttonGuardar.setOnClickListener {
            agregarRecurso()
        }
    }

    private fun agregarRecurso() {
        val titulo = editTextTitulo.text.toString().trim()
        val descripcion = editTextDescripcion.text.toString().trim()
        val tipo = editTextTipo.text.toString().trim()
        val enlace = editTextEnlace.text.toString().trim()
        val imagen = editTextImagen.text.toString().trim()

        if (titulo.isEmpty() || descripcion.isEmpty() || tipo.isEmpty() || enlace.isEmpty() || imagen.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoRecurso = Recurso(
            id = 0,
            titulo = titulo,
            descripcion = descripcion,
            tipo = tipo,
            enlace = enlace,
            imagen = imagen
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://6705a449031fd46a8310a102.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        val call = api.crearRecurso(nuevoRecurso)

        call.enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AgregarRecurso, "Recurso agregado con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AgregarRecurso, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@AgregarRecurso, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}