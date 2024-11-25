package com.example.projetointegrador

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projetointegrador.databinding.ActivityCadastrarViagemBinding

class CadastrarViagem : AppCompatActivity() {

    private lateinit var binding:ActivityCadastrarViagemBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarViagemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.btnCadastrar.setOnClickListener {
            if (validarCampos()) {
                inserirViagem()
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun validarCampos(): Boolean {
        val titulo = binding.Titulo.text.toString().trim()
        val data = binding.Data.text.toString().trim()
        val retorno = binding.Retorno.text.toString().trim()
        val destino = binding.Destino.text.toString().trim()
        val placa = binding.Placa.text.toString().trim()
        val km = binding.km.text.toString().trim()

        if (titulo.isEmpty()) {
            exibirAlerta("O campo Título não pode estar vazio.")
            return false
        }

        if (data.isEmpty()) {
            exibirAlerta("O campo Data não pode estar vazio.")
            return false
        }

        if (retorno.isEmpty()) {
            exibirAlerta("O campo Retorno não pode estar vazio.")
            return false
        }

        if (destino.isEmpty()) {
            exibirAlerta("O campo Destino não pode estar vazio.")
            return false
        }


        if (placa.isEmpty()) {
            exibirAlerta("O campo Placa não pode estar vazio.")
            return false
        }

        if (km.isEmpty()) {
            exibirAlerta("O campo KM não pode estar vazio.")
            return false
        }

        return true
    }

    private fun inserirViagem() {
        val titulo = binding.Titulo.text.toString().trim()
        val data = binding.Data.text.toString().trim()
        val retorno = binding.Retorno.text.toString().trim()
        val destino = binding.Destino.text.toString().trim()
        val placa = binding.Placa.text.toString().trim()
        val km = binding.km.text.toString().trim().toInt()
        val abastecido = binding.abastecido.isChecked

        val id = dbHelper.inserirRegistro(
            titulo, data, retorno, destino, placa, km, abastecido
        )

        if (id != -1L) {
            Toast.makeText(this, "Viagem cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
            limparCampos()

            val intent = Intent(this, Home::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Erro ao cadastrar a viagem.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limparCampos() {
        binding.Titulo.text.clear()
        binding.Data.text.clear()
        binding.Retorno.text.clear()
        binding.Destino.text.clear()
        binding.Placa.text.clear()
        binding.km.text.clear()
        binding.abastecido.isChecked = false
    }

    private fun exibirAlerta(mensagem: String) {
        AlertDialog.Builder(this)
            .setTitle("Atenção")
            .setMessage(mensagem)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}