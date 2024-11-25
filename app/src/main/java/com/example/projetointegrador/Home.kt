package com.example.projetointegrador

import android.content.Intent
import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.projetointegrador.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        val viagens = dbHelper.getViagens()

        carregarViagens()

        binding.NovaViagem.setOnClickListener {
            val intent = Intent(this, CadastrarViagem::class.java)
            startActivity(intent)
        }

        binding.listViewViagens.setOnItemClickListener { _, _, position, _ ->
            val viagem = viagens[position]
            val intent = Intent(this, DetalhesViagemActivity::class.java)
            intent.putExtra("VIAGEM_ID", viagem.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        carregarViagens()
    }

    private fun carregarViagens() {
        val viagens = dbHelper.getViagens()

        val adapterData = viagens.map {
            mapOf(
                "titulo" to it.titulo,
                "destino" to "Para: ${it.destino}"
            )
        }

        val adapter = SimpleAdapter(
            this,
            adapterData,
            android.R.layout.simple_list_item_2,
            arrayOf("titulo", "destino"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        binding.listViewViagens.adapter = adapter
    }
}