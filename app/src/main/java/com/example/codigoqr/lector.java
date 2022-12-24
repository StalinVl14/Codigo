package com.example.codigoqr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class lector extends AppCompatActivity {

    TextView txtResultado;
    Button btncam, btnReconigtion;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector);

        txtResultado = findViewById(R.id.txtDataEquipo);
        imgView = findViewById(R.id.imgViewCamara);
        btncam = (Button) findViewById(R.id.btncam);
        btnReconigtion = (Button) findViewById(R.id.btnReconocer);

        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamara();
            }
        });

        btnReconigtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(lector.this, camara.class);
                startActivity(i);
            }
        });

        initQrLector();
    }

    private void initQrLector(){
        // Iniciar el lector QR
        IntentIntegrator integrator = new IntentIntegrator( lector.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Lector - QR");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
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

        if (requestCode == 1 & resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imgBitmap);
        }
    }
}