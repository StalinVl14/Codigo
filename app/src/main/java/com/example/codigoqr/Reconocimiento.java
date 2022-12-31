package com.example.codigoqr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Reconocimiento extends AppCompatActivity {

    TextView txtNamePlayer, txtCedulaPlayer, txtNameTeamPlayer;
    Button btninicio, btnequipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconocimiento);

        txtNamePlayer = findViewById(R.id.txtNamePlayer);
        txtCedulaPlayer = findViewById(R.id.txtCedulaPlayer);
        txtNameTeamPlayer = findViewById(R.id.txtNameTeamPlayer);

        btninicio = findViewById(R.id.btninicio);
        btnequipo = findViewById(R.id.btnequipo);


        String nameCedula = getIntent().getStringExtra("name");
        String img64 = getIntent().getStringExtra("img");
        String format = getIntent().getStringExtra("format");
        String name_team = getIntent().getStringExtra("name_team");

        //toaquiza_chimborazo_oscar_matias_0551032188

        String cedula = nameCedula.substring(nameCedula.length() - 10);
        String nombreApellidos = nameCedula.substring(0, nameCedula.length() - 11);

        if (nombreApellidos.contains("_")) {
            String nombreApellidosSeparado = nombreApellidos.replaceAll("_", " ");
            txtNamePlayer.setText(nombreApellidosSeparado.toUpperCase());
        } else {
            txtNamePlayer.setText(nombreApellidos);
        }
        txtCedulaPlayer.setText(cedula);
        txtNameTeamPlayer.setText(name_team);


        btninicio = (Button) findViewById(R.id.btninicio);
        btninicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Reconocimiento.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnequipo = (Button) findViewById(R.id.btnequipo);
        btnequipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Reconocimiento.this, lector.class);
                i.putExtra("start_lector", false);
                startActivity(i);

            }
        });
    }
}