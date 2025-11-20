package com.example.dilemario.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// ----------------------------
// REQUESTS / RESPONSES LOGIN
// ----------------------------
data class LoginRequest(
    val email: String,
    val password: String
)

data class UserData(
    val id: Int,
    val nombre: String,
    val email: String,
    val edad_rango: String,
    val pais: String
)

data class LoginData(
    val user: UserData,
    val token: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val data: LoginData?
)


// ----------------------------
// MODELOS PARA DILEMAS (según tu JSON EXACTO)
// ----------------------------
data class DilemaApi(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val opcion_a: String,
    val opcion_b: String,
    val categoria: String,
    val creado_por: Int?,
    val activo: Boolean,
    val created_at: String,
    val total_denuncias: Int,
    val creador_nombre: String?
)

data class DilemaListResponse(
    val success: Boolean,
    val data: List<DilemaApi>
)


// ----------------------------
// API SERVICE
// ----------------------------
interface ApiService {

    // LOGIN
    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse


    @GET("api/dilemas/{userId}/unanswered")
    suspend fun getDilemasUnanswered(@retrofit2.http.Path("userId") userId: Int): DilemaListResponse

}
