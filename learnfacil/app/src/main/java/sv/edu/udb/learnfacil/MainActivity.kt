package sv.edu.udb.learnfacil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast // Importar Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sv.edu.udb.learnfacil.model.Recurso

class MainActivity : AppCompatActivity() {

    private lateinit var api: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var recursoAdapter: RecursoAdapter
    private var listaRecursos: List<Recurso> = listOf()
    private lateinit var editTextRecursoId: EditText
    private lateinit var buttonBuscarRecurso: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://6705a449031fd46a8310a102.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ApiService::class.java)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        editTextRecursoId = findViewById(R.id.editTextRecursoId)
        buttonBuscarRecurso = findViewById(R.id.buttonBuscarRecurso)

        obtenerRecursos()

        // Agregar Recurso
        val fabAddRecurso: FloatingActionButton = findViewById(R.id.fabAddRecurso)
        fabAddRecurso.setOnClickListener {
            val intent = Intent(this, AgregarRecurso::class.java)
            startActivity(intent)
        }

        // Refresh
        val fabRefresh: FloatingActionButton = findViewById(R.id.fabRefresh)
        fabRefresh.setOnClickListener {
            obtenerRecursos()
        }

        // Buscar por ID
        buttonBuscarRecurso.setOnClickListener {
            val id = editTextRecursoId.text.toString().toIntOrNull()
            if (id != null) {
                buscarRecursoPorId(id)
            } else {
                Toast.makeText(this, "ID no válido", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun obtenerRecursos() {
        val call = api.obtenerRecursos()
        call.enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    listaRecursos = response.body() ?: emptyList()
                    recursoAdapter = RecursoAdapter(this@MainActivity, listaRecursos)
                    recyclerView.adapter = recursoAdapter
                } else {
                    Toast.makeText(this@MainActivity, "Error en la respuesta: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error en la llamada: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun buscarRecursoPorId(id: Int) {
        val call = api.obtenerRecursosPorId(id)
        call.enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    val recurso = response.body()
                    if (recurso != null) {
                        listaRecursos = listOf(recurso)
                        recursoAdapter = RecursoAdapter(this@MainActivity, listaRecursos)
                        recyclerView.adapter = recursoAdapter
                        Toast.makeText(this@MainActivity, "Recurso encontrado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Recurso no encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "ID no encontrado: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error en la llamada: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
