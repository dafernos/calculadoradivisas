package com.example.calculadoradivisas;


import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;



public class MainActivity extends AppCompatActivity {
    private RadioButton r1, r2;
    private String moneda, url;
    private TextView pantalla, pantalla2;
    private static String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        r1 = findViewById(R.id.radioButton);
        r2 = findViewById(R.id.radioButton2);
        pantalla = findViewById(R.id.pantalla1);
        pantalla2 = findViewById(R.id.pantalla2);


    }

    public void exdiv(View view) {
        if (r1.isChecked() == true)
            moneda="USD_EUR";
        if (r2.isChecked() == true)
            moneda="EUR_USD";
    }

    public void onClick(View view){
        String value = ((Button) view).getText().toString();
        switch (view.getId()) {
            case R.id.b12:
                pantalla.setText("0");
                break;
            case R.id.b10:
                if (pantalla.getText().toString().equals("0"))
                    pantalla.setText("0" + value);
                else if (pantalla.getText().toString().contains(".")) pantalla.append("");
                else pantalla.append(value);
                break;
            default:
                if (pantalla.getText().toString().startsWith("0") && !pantalla.getText().toString().startsWith("0."))
                    pantalla.setText("" + value);
                else if (pantalla.getText().toString().startsWith("0."))
                    pantalla.append(value);
                else pantalla.append(value);
                break;
        }
        exdiv(view);
        exchange();
    }
    private void exchange() {
        url = "http://free.currencyconverterapi.com/api/v5/convert?q="+moneda+"&compact=ultra";
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    // Parsing json object response
                    // response will be a json object
;
                    String jsonResponse = response.getString(moneda);
                    Double operacion= Double.parseDouble(jsonResponse.toString())*Double.parseDouble(pantalla.getText().toString());
                    String resultado = String.format("%.2f",operacion);
                    if (moneda.contains("USD_EUR")){
                        pantalla2.setText(resultado+"€");
                    }
                    if (moneda.contains("EUR_USD")){
                        pantalla2.setText(resultado+"$");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


}
