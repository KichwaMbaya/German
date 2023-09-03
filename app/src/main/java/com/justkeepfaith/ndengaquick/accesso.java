package com.justkeepfaith.ndengaquick;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class accesso extends AppCompatActivity {

    EditText lgnemail, lgnpin;
    Button lgnlogin;
    TextView lgncreate;
    SharedPreferences sharedPreferences;
    String emaillo, passworddo, reg_email;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accesso);

        lgnemail = findViewById(R.id.lgnemail);
        lgnpin = findViewById(R.id.lgnpin);
        lgnlogin = findViewById(R.id.lgnlogin);
        lgncreate = findViewById(R.id.lgncreate);


        sharedPreferences = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        Integer status = sharedPreferences.getInt("logged", 0);
        reg_email = sharedPreferences.getString("Email", "");

        if (status == 2){

            if (reg_email.isEmpty()){

                /*Intent intent = new Intent(accesso.this, in_progress.class);
                startActivity(intent);
                finish();*/
            }else {

                Intent intent = new Intent(accesso.this, finitura.class);
                startActivity(intent);
                finish();
            }
        }

        lgnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginacc();
            }
        });
        lgncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(accesso.this, registrati.class);
                startActivity(intent);
            }
        });
    }
    private void loginacc(){
        String emaillog = lgnemail.getText().toString();
        String lgnpinlog = lgnpin.getText().toString();
        String devemail = "developers@android.com";

        emaillo = sharedPreferences.getString("Email", "developers@android.com");
        passworddo = sharedPreferences.getString("PIN", "6969");

        if (emaillog.isEmpty()){
            lgnemail.setError("E-mail eingeben");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emaillog).matches()){
            lgnemail.setError("Ungültige E-Mail");
            lgnemail.requestFocus();
            return;
        }
        if (lgnpinlog.isEmpty()){
            lgnpin.setError("Kann nicht leer sein");
            return;
        }
        if (lgnpinlog.length() < 4){
            lgnpin.setError("Zu kurz");
            return;
        }
        if (!emaillog.equals(emaillo)){
            lgnemail.setError("Falsche E-Mail Adresse");
            Toast.makeText(accesso.this, "Falsche E-Mail Adresse", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!lgnpinlog.equals(passworddo)){
            lgnpin.setError("Wrong PIN");
            Toast.makeText(accesso.this, "Wrong PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Integer logged_in = 2;
        editor.putInt("logged", logged_in);
        editor.commit();

        progressDialog = new ProgressDialog(accesso.this);
        progressDialog.setMessage("Einloggen...");
        progressDialog.setProgressStyle(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    if (emaillog.equals(devemail)){
                        /*Intent intent = new Intent(accesso.this, in_progress.class);
                        startActivity(intent);
                        finish();*/
                    } else if (reg_email.isEmpty()) {
                        Intent intent = new Intent(accesso.this, limite_premio.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(accesso.this, finitura.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }
}