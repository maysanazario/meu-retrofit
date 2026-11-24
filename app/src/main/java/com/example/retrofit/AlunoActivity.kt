package com.example.retrofit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class AlunoActivity : AppCompatActivity() {

    private val ui = CoroutineScope(Dispatchers.Main)
    private val itens = mutableListOf<String>()
    private val alunos = mutableListOf<Aluno>()
    private lateinit var adaptador: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aluno)

        val edtId: EditText = findViewById(R.id.edtIdAluno)
        val edtNome: EditText = findViewById(R.id.edtNomeAluno)
        val edtEmail: EditText = findViewById(R.id.edtEmailAluno)
        val edtCurso: EditText = findViewById(R.id.edtCursoAluno)
        val btnSalvar: Button = findViewById(R.id.btnSalvarAluno)
        val btnAtualizar: Button = findViewById(R.id.btnAtualizarAluno)
        val btnExcluir: Button = findViewById(R.id.btnExcluirAluno)
        val btnListar: Button = findViewById(R.id.btnListarAlunos)
        val lista: ListView = findViewById(R.id.listaAlunos)

        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, itens)
        lista.adapter = adaptador

        // Tocar num item preenche os campos (facilita atualizar/excluir)
        lista.setOnItemClickListener { _, _, pos, _ ->
            val a = alunos[pos]
            edtId.setText(a.id_aluno?.toString() ?: "")
            edtNome.setText(a.nome)
            edtEmail.setText(a.email)
            edtCurso.setText(a.curso)
        }

        btnSalvar.setOnClickListener {
            val a = Aluno(
                nome = edtNome.text.toString(),
                email = edtEmail.text.toString(),
                curso = edtCurso.text.toString()
            )
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.criarAluno(a) }
                    toast("Cadastrado com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtCurso)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao salvar: ${e.message}")
                }
            }
        }

        btnAtualizar.setOnClickListener {
            val id = edtId.text.toString().toIntOrNull()
            if (id == null) { toast("Informe um ID para atualizar"); return@setOnClickListener }
            val a = Aluno(
                nome = edtNome.text.toString(),
                email = edtEmail.text.toString(),
                curso = edtCurso.text.toString()
            )
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.atualizarAluno(id, a) }
                    toast("Atualizado com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtCurso)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao atualizar: ${e.message}")
                }
            }
        }

        btnExcluir.setOnClickListener {
            val id = edtId.text.toString().toIntOrNull()
            if (id == null) { toast("Informe um ID para excluir"); return@setOnClickListener }
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.excluirAluno(id) }
                    toast("Excluído com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtCurso)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao excluir: ${e.message}")
                }
            }
        }

        btnListar.setOnClickListener { listar() }
        listar()
    }

    private fun listar() {
        ui.launch {
            try {
                val dados = withContext(Dispatchers.IO) { RetrofitClient.api.listarAlunos() }
                alunos.clear(); alunos.addAll(dados)
                itens.clear()
                dados.forEach { a ->
                    itens.add("#${a.id_aluno} - ${a.nome} | ${a.email} | ${a.curso}")
                }
                adaptador.notifyDataSetChanged()
            } catch (e: Exception) {
                toast("Erro ao listar: ${e.message}")
            }
        }
    }

    private fun limpar(vararg campos: EditText) = campos.forEach { it.setText("") }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    override fun onDestroy() {
        super.onDestroy()
        ui.cancel()
    }
}