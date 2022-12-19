package com.example.codigoqr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class lector extends AppCompatActivity {

    EditText txtResultado;
    Button btncam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector);

        btncam= findViewById(R.id.btncam);
        txtResultado = findViewById(R.id.txtResultado);

        btncam= (Button) findViewById(R.id.btncam);
        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lector.this, camara.class);
                startActivity(i);
                abrirCamara();

            }
        });
    }

    private void abrirCamara (){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 1 );
        }

    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        IntentResult result  = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result !=null){
            if(result.getContents()== null){
                Toast.makeText(this,"Lectura cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,result.getContents(), Toast.LENGTH_LONG).show();
                txtResultado.setText(result.getContents());
            }
        }else {

            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}