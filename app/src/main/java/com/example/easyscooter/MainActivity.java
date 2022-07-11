package com.example.easyscooter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private Button login_btn;
    private EditText login_txt, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.login_btn = findViewById(R.id.login_btn);
        this.login_txt = findViewById(R.id.login_txt);
        this.password = findViewById(R.id.pwd);


        this.login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = login_txt.getText().toString();
                String pwd = password.getText().toString();

                if(email.trim().length() > 0 && pwd.trim().length() > 0){

                    try {
                        RequestQueue file = Volley.newRequestQueue(MainActivity.this);
                        String URL = "http://192.168.1.12/easyscooter/api_easyscooter/API/login";
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("email", email);
                        jsonBody.put("password", pwd);
                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("REQUETE", response);

                                try{

                                JSONObject global = new JSONObject(response);
                                JSONObject user_data = global.getJSONObject("user");

                                if (!global.getBoolean("trotinette")){
                                    Toast.makeText(MainActivity.this, "Hey, tu n'utilises aucune trotinette en ce moment!", Toast.LENGTH_SHORT).show();
                                }else {
                                    JSONObject trotinette_data = global.getJSONObject("trotinette");



                                    UserLogResponse u = new UserLogResponse();
                                    u.setIdTrotinette(trotinette_data.getInt("id"));
                                    u.setFirstname(user_data.getString("firstname"));
                                    u.setLastname(user_data.getString("lastname"));

                                    Intent i = new Intent(MainActivity.this, UserActivity.class);
                                    i.putExtra("UserResponse", u);
                                    startActivity(i);

                                }
                                    } catch(Exception e){
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQUETE", error.toString());
                                Toast.makeText(MainActivity.this, "Mauvais identifiants de connexion, veuillez ré-eassayez.", Toast.LENGTH_SHORT).show();
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
                                return super.parseNetworkResponse(response);
                            }
                        };

                        file.add(stringRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "Saisie mail et mot de passe obligatoire !", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
}