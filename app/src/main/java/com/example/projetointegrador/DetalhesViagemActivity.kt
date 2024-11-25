package com.example.projetointegrador

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projetointegrador.databinding.ActivityDetalhesViagemBinding
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetalhesViagemActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding: ActivityDetalhesViagemBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var googleMap: GoogleMap
    private var destinoCidade: String? = null
    private var viagemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesViagemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        viagemId = intent.getIntExtra("VIAGEM_ID", -1)

        val viagemId = intent.getIntExtra("VIAGEM_ID", -1)

        if (viagemId != -1) {
            val viagem = dbHelper.getViagemById(viagemId)
            if (viagem != null) {
                displayViagemDetails(viagem)
            } else {
                Toast.makeText(this, "Viagem não encontrada!", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Erro ao carregar a viagem!", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnExcluir.setOnClickListener {
            excluirViagem()
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        val cidade = destinoCidade

        if (cidade != null) {
            val geocoder = Geocoder(this)
            val addressList = geocoder.getFromLocationName(cidade, 1) // Máximo de 1 resultado

            if (!addressList.isNullOrEmpty()) {
                val location = addressList[0]
                val latLng = LatLng(location.latitude, location.longitude)

                googleMap.addMarker(MarkerOptions().position(latLng).title("Cidade: $cidade"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
            } else {
                Toast.makeText(this, "Não foi possível encontrar a localização da cidade", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Destino não encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayViagemDetails(viagem: Viagem) {
        binding.textTitulo.text = viagem.titulo
        binding.textData.text = "Data: ${viagem.data}"
        binding.textDataRetorno.text = "Retorno: ${viagem.dataRetorno}"
        binding.textDestino.text = "Destino: ${viagem.destino}"
        binding.textPlaca.text = "Placa: ${viagem.placaVeiculo}"
        binding.textKm.text = "KM Total: ${viagem.kmTotal}"
        binding.textAbastecido.text = if (viagem.abastecido) "Abastecido: Sim" else "Abastecido: Não"
        destinoCidade = viagem.destino
    }

    private fun excluirViagem() {
        if (viagemId != -1) {
            val result = dbHelper.deletarViagem(viagemId)
            if (result > 0) {
                Toast.makeText(this, "Viagem excluída com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Erro ao excluir a viagem!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "ID da viagem inválido!", Toast.LENGTH_SHORT).show()
        }
    }
}