package com.justkeepfaith.ndengaquick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class profilo extends AppCompatActivity {

    TextView user_details, bankaccount;
    Button editbank, deleteaccount;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        user_details = findViewById(R.id.user_details);
        bankaccount = (TextView) findViewById(R.id.bankaccount);
        editbank = findViewById(R.id.editbank);
        deleteaccount = findViewById(R.id.deleteaccount);

        sharedPreferences = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        String string = sharedPreferences.getString("first_name", "");
        String string2 = sharedPreferences.getString("middle_name", "");
        String string3 = sharedPreferences.getString("last_name", "");
        String string4 = sharedPreferences.getString("Email", "");
        String string5 = sharedPreferences.getString("phone_number", "");
        String string6 = sharedPreferences.getString("occupation", "");
        String string7 = sharedPreferences.getString("workplace", "");
        String string8 = sharedPreferences.getString("income", "");
        String string9 = sharedPreferences.getString("IDno", "");
        String string10 = sharedPreferences.getString("keen_name", "");
        String string11 = sharedPreferences.getString("relationship", "");
        String string12 = sharedPreferences.getString("keen_phone", "");

        String Routing = sharedPreferences.getString("r_number", "");
        String Account_number = sharedPreferences.getString("a_number", "");

        user_details.setText("\n1. Name: "+ string + " " + string2 + " " + string3 +
                "\n\n2. E-mail: " + string4 +
                "\n\n3. Telefonnummer: " + string5 +
                "\n\n4. Beschäftigung: " + string6 +
                "\n\n5. Arbeitsplatz: " + string7 +
                "\n\n6. Personenkennziffer: " + string9 +
                "\n\n7. Garant: " + string10 +
                "\n\n8. Beziehung: " + string11 +
                "\n\n9. Telefonnummer: " +string12 + "\n");


        bankaccount.setText(Account_number);

        editbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilo.this, bancarie_coordinate.class);
                startActivity(intent);
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(profilo.this);
                dialog.setTitle("Bist du sicher?");
                dialog.setMessage("Das Löschen dieses Kontos führt zur vollständigen Entfernung Ihrer Daten aus dem System. Diese Aktion kann " +
                        "nicht rückgängig gemacht werden und Sie können nicht mehr auf die App zugreifen.\n\n" +
                        "Bitte beachten Sie, dass Ihnen etwaiges Guthaben auf Ihrem Konto innerhalb von 5 Werktagen zurückerstattet wird.");
                dialog.setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!Conectivity.isConnectingToInternet(profilo.this)) {
                            Toast.makeText(profilo.this, "Bitte überprüfe deine Internetverbindung", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        Toast.makeText(profilo.this, "Gelöscht", Toast.LENGTH_SHORT).show();

                        android.os.Process.killProcess(android.os.Process.myPid());

                    }
                });
                dialog.setNegativeButton("Absagen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }
}