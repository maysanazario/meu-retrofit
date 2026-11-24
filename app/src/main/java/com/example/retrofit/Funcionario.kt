package com.example.retrofit

data class Funcionario(
    val id_funcionario: Int? = null,
    var nome: String = "",
    var email: String = "",
    var senha: String = "",
    var cargo: String = ""
)