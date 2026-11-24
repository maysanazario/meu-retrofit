package com.example.retrofit
import retrofit2.http.*

interface ApiService {
    @GET("funcionarios")
    suspend fun listar(): List<Funcionario>
    @GET("funcionarios/{id}")
    suspend fun obter(@Path("id") id: Int): Funcionario
    @POST("funcionarios")
    suspend fun criar(@Body f: Funcionario): Map<String, String> // message
    @PUT("funcionarios/{id}")
    suspend fun atualizar(@Path("id") id: Int, @Body f: Funcionario): Map<String, String>
    @DELETE("funcionarios/{id}")
    suspend fun excluir(@Path("id") id: Int): Map<String, String>

    @GET("alunos")
    suspend fun listarAlunos(): List<Aluno>

    @GET("alunos/{id}")
    suspend fun obterAluno(@Path("id") id: Int): Aluno

    @POST("alunos")
    suspend fun criarAluno(@Body a: Aluno): Map<String, String>

    @PUT("alunos/{id}")
    suspend fun atualizarAluno(@Path("id") id: Int, @Body a: Aluno): Map<String, String>

    @DELETE("alunos/{id}")
    suspend fun excluirAluno(@Path("id") id: Int): Map<String, String>


}
