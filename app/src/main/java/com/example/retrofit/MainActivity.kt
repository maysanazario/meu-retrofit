package com.example.retrofit
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import kotlinx.coroutines.*
class MainActivity : AppCompatActivity() {

    private val ui = CoroutineScope(Dispatchers.Main)
    private var itens = mutableListOf<String>()
    private lateinit var adaptador: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mapeamento direto
        val edtId: EditText = findViewById(R.id.edtId)
        val edtNome: EditText = findViewById(R.id.edtNome)
        val edtEmail: EditText = findViewById(R.id.edtEmail)
        val edtSenha: EditText = findViewById(R.id.edtSenha)
        val edtCargo: EditText = findViewById(R.id.edtCargo)
        val btnSalvar: Button = findViewById(R.id.btnSalvar)
        val btnAtualizar: Button = findViewById(R.id.btnAtualizar)
        val btnExcluir: Button = findViewById(R.id.btnExcluir)
        val btnListar: Button = findViewById(R.id.btnListar)
        val lista: ListView = findViewById(R.id.lista)

        findViewById<Button>(R.id.btnAlunos).setOnClickListener {
            startActivity(android.content.Intent(this, AlunoActivity::class.java))
        }

        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, itens)
        lista.adapter = adaptador

        // Botões
        btnSalvar.setOnClickListener {
            val f = Funcionario(
                nome = edtNome.text.toString(),
                email = edtEmail.text.toString(),
                senha = edtSenha.text.toString(),
                cargo = edtCargo.text.toString()
            )
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.criar(f) }
                    toast("Cadastrado com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtSenha, edtCargo)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao salvar: ${e.message}")
                }
            }
        }

        btnAtualizar.setOnClickListener {
            val id = edtId.text.toString().toIntOrNull()
            if (id == null) {
                toast("Informe um ID para atualizar")
                return@setOnClickListener
            }
            val f = Funcionario(
                nome = edtNome.text.toString(),
                email = edtEmail.text.toString(),
                senha = edtSenha.text.toString(),
                cargo = edtCargo.text.toString()
            )
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.atualizar(id, f) }
                    toast("Atualizado com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtSenha, edtCargo)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao atualizar: ${e.message}")
                }
            }
        }

        btnExcluir.setOnClickListener {
            val id = edtId.text.toString().toIntOrNull()
            if (id == null) {
                toast("Informe um ID para excluir")
                return@setOnClickListener
            }
            ui.launch {
                try {
                    withContext(Dispatchers.IO) { RetrofitClient.api.excluir(id) }
                    toast("Excluído com sucesso")
                    limpar(edtId, edtNome, edtEmail, edtSenha, edtCargo)
                    listar()
                } catch (e: Exception) {
                    toast("Erro ao excluir: ${e.message}")
                }
            }
        }

        btnListar.setOnClickListener {
            listar()
        }

        // já lista ao abrir
        listar()
    }

    private fun listar() {
        ui.launch {
            try {
                val dados = withContext(Dispatchers.IO) { RetrofitClient.api.listar() }
                itens.clear()
                dados.forEach { f ->
                    itens.add("#${f.id_funcionario} - ${f.nome} | ${f.email} | ${f.cargo}")
                }
                adaptador.notifyDataSetChanged()
            } catch (e: Exception) {
                toast("Erro ao listar: ${e.message}")
            }
        }
    }
    private fun limpar(vararg campos: EditText) {
        campos.forEach { it.setText("") }
    }
    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}