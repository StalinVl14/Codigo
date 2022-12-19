package com.example.codigoqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Reconocimiento extends AppCompatActivity {
    Button btninicio;
    Button btnequipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconocimiento);

        btninicio = findViewById(R.id.btninicio);

        btninicio= (Button) findViewById(R.id.btninicio);
        btninicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(Reconocimiento.this, MainActivity.class);
                    startActivity(i);
            }
        });

        btnequipo = findViewById(R.id.btnequipo);
        
        btnequipo=(Button) findViewById(R.id.btnequipo);
        btnequipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Reconocimiento.this, lector.class);
                startActivity(i);

            }
        });
    }
}