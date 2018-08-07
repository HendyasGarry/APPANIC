package com.example.hendyasgarry.appanic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterAct extends AppCompatActivity {

    FirebaseAuth auth;
    String uEmail, pNumber, pass, repas;
    TextView nullEmail, errPass, errCheck, errNumb;
    EditText txtName, txtEmail, txtPass, txtRepas, txtNumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail = (EditText) findViewById(R.id.eMail);
        txtPass = (EditText) findViewById(R.id.password);
        txtRepas = (EditText) findViewById(R.id.reinput);
        txtNumb = (EditText) findViewById(R.id.pNumber);

        nullEmail = (TextView) findViewById(R.id.nullEmail);
        errPass = (TextView) findViewById(R.id.errorPass);
        errCheck = (TextView) findViewById(R.id.errorCheck);
        errNumb = (TextView) findViewById(R.id.errorNumb);

        auth = FirebaseAuth.getInstance();
    }


    public void register(View view) {
        uEmail = txtEmail.getText().toString().trim();
        pass = txtPass.getText().toString().trim();
        repas = txtRepas.getText().toString().trim();
        pNumber = txtNumb.getText().toString().trim();

        if(nullchecker()){
            //Toast.makeText(this, "failed to register, check your internet connection", Toast.LENGTH_SHORT).show();
            auth.createUserWithEmailAndPassword(uEmail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }else{
                                //display some message here
                                Toast.makeText(RegisterAct.this
                                        ,"Registration Error, check your internet connection"
                                        ,Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean nullchecker() {
        nullEmail.setVisibility(View.GONE);
        errPass.setVisibility(View.GONE);
        errCheck.setVisibility(View.GONE);
        errNumb.setVisibility(View.GONE);

        if(uEmail.isEmpty()){
            nullEmail.setVisibility(View.VISIBLE);
            return false;
        }
        if(pass.length()<8){
            errPass.setVisibility(View.VISIBLE);
            return false;
        }
        /*cek passwordnya*/
        if(!repas.equals(pass)){
            errCheck.setVisibility(View.VISIBLE);
            return false;
        }
        if(pNumber.length()<10){
            errNumb.setVisibility(View.VISIBLE);
            return false;
        }
        if(pNumber.length()>12){
            errNumb.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
