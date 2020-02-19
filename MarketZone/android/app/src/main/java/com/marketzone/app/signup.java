package com.marketzone.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    String username, firstname, lastname, phone, email, password;
    EditText username_, firstname_, lastname_, phone_, email_, password_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username_ = (EditText) findViewById(R.id.edittext_username);
        firstname_ = (EditText) findViewById(R.id.edittext_firstname);
        lastname_ = (EditText) findViewById(R.id.edittext_lastname);
        phone_ = (EditText) findViewById(R.id.edittext_phone);
        email_ = (EditText) findViewById(R.id.edittext_Email);
        password_ = (EditText) findViewById(R.id.edittext_pass);

        Button submit = (Button) findViewById(R.id.register);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = username_.getText().toString();
                firstname = firstname_.getText().toString();
                lastname = lastname_.getText().toString();
                phone = phone_.getText().toString();
                email = email_.getText().toString();
                password = password_.getText().toString();
                addUser();
            }
        });

    }

    private void addUser() {
        String url = "http://10.40.46.65:3000/project/createUser/";
        Map<String, String> params = new HashMap();
        params.put("username", username);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("phone", phone);
        params.put("email", email);
        params.put("pass", password);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO: handle success
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }
}
