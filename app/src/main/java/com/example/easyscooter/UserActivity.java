package com.example.easyscooter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UserActivity extends AppCompatActivity {
    private EditText comment_txt;
    private TextView item_name, user_name;
    private Button btn_send;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        this.comment_txt = findViewById(R.id.comment_txt);
        this.btn_send = findViewById(R.id.sendAPI_btn);
        this.item_name = findViewById(R.id.item_name);
        this.user_name = findViewById(R.id.user_name);

        Intent i = getIntent();
        UserLogResponse u = (UserLogResponse)i.getSerializableExtra("UserResponse");
        item_name.setText("Trotinette n°"+u.getIdTrotinette());
        user_name.setText("Salut "+u.getFirstname()+" !");



        this.btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = comment_txt.getText().toString();

                if (comment.length() > 0){
                    try {
                        RequestQueue file = Volley.newRequestQueue(UserActivity.this);
                        String URL = "https://easy-scooter.fr/api_easyscooter/API/trotinette";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("comment", comment);
                        jsonBody.put("is_functional", 0);
                        jsonBody.put("id", u.getIdTrotinette());
                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("REQUETE", response);
                                try{

                                    Intent i = new Intent(UserActivity.this, FinalActivity.class);
                                    startActivity(i);

                                } catch(Exception e){
                                    Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQUETE", error.toString());
                                Toast.makeText(UserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String responseString = "";
                                if (response != null) {
                                    responseString = String.valueOf(response.statusCode);
                                    // can get more details such as response.headers
                                }
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                        };

                        file.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(UserActivity.this, "Saisie  obligatoire !", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}