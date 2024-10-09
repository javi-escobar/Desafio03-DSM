package sv.edu.udb.learnfacil

import retrofit2.Call
import retrofit2.http.*
import sv.edu.udb.learnfacil.model.Recurso

interface ApiService {
    @GET("learnfacil/learnfacil")
    fun obtenerRecursos () : Call<List<Recurso>>
    @GET("learnfacil/learnfacil/{id}")
    fun obtenerRecursosPorId(@Path("id") id: Int) : Call<Recurso>
    @POST("learnfacil/learnfacil")
    fun crearRecurso(@Body recurso: Recurso) : Call<Recurso>
    @PUT("learnfacil/learnfacil/{id}")
    fun actualizarRecurso(@Path("id") id: Int, @Body recurso: Recurso) : Call<Recurso>
    @DELETE("learnfacil/learnfacil/{id}")
    fun eliminarRecurso(@Path("id") id: Int): Call<Void>

}