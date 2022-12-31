package com.example.codigoqr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.codigoqr.clases.DataQr;
import com.example.codigoqr.clases.NetworkClient;
import com.example.codigoqr.clases.UploadApis;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class lector extends AppCompatActivity {

    TextView txtNameLiga, txtTemaporadaLiga, txtNameTeam;
    Button btncam, btnReconigtion;
    ImageView imgView;
    Bitmap imgBitmap;
    Boolean startLector;
    final static String SUCCESS = "success";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector);

        startLector = getIntent().getBooleanExtra("start_lector",true);

        txtNameLiga = findViewById(R.id.txtNameLiga);
        txtTemaporadaLiga = findViewById(R.id.txtTemporadaLiga);
        txtNameTeam = findViewById(R.id.txtNameTeam);

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
                uploadImage();
            }
        });

        btnReconigtion.setVisibility(View.GONE);

        if(startLector){
            initQrLector();
        }

    }

    private void initQrLector() {
        // Iniciar el lector QR
        IntentIntegrator integrator = new IntentIntegrator(lector.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Lector - QR - MASHKAFUTBOL");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                try {
                    getDataQr(result.getContents());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == 1 & resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imgBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imgBitmap);
            btnReconigtion.setVisibility(View.VISIBLE);
        }
    }

    private void getDataQr(String json_string) throws JSONException {
        JSONObject root = new JSONObject(json_string);
        txtNameLiga.setText(root.getString("nombre_liga"));
        txtTemaporadaLiga.setText(root.getString("temporada_liga"));
        txtNameTeam.setText(root.getString("nombre_equipo"));
    }

    private void uploadImage() {
        File file = getBitmapToFile(imgBitmap);
        if (file == null) {
            Toast.makeText(this, "ERROR FILE IS NULL", Toast.LENGTH_LONG).show();
        } else {


            Retrofit retrofit = NetworkClient.getRetrofit();

            RequestBody requesFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part parts = MultipartBody.Part.createFormData("foto", file.getName(), requesFile);

//        RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");

//            Call<ResponseBody> call = u

            UploadApis uploadApis = retrofit.create(UploadApis.class);
            Call call = uploadApis.uploadImage(parts);
            call.enqueue(new Callback<DataQr>() {
                @Override
                public void onResponse(Call<DataQr> call, Response<DataQr> response) {
                    System.out.println(response);
                    if(response.isSuccessful()) {
                        String name = response.body().getName();
                        String msg = response.body().getMsg();

                        if(msg.equals(SUCCESS)){
                            Intent i = new Intent(lector.this, Reconocimiento.class);
                            i.putExtra("name",name);
                            i.putExtra("img",response.body().getImg());
                            i.putExtra("format",response.body().getFormat());
                            i.putExtra("name_team","EQUIPO" );
                            startActivity(i);
                        }else{
                            Toast.makeText(getApplicationContext(), msg + " > " + name , Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    System.out.println(t.getMessage());
                    //Toast.makeText(this, "ERROR FILE IS NULL", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private File getBitmapToFile(Bitmap bitmap) {
        File file = new File(getApplicationContext().getCacheDir() + "imgws.png");

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        try {
            //write the bytes in file
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}