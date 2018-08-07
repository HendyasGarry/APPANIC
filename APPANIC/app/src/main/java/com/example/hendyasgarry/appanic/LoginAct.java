package com.example.hendyasgarry.appanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAct extends AppCompatActivity {

    FirebaseAuth auth;
    EditText textEmail, textPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textEmail = (EditText) findViewById(R.id.mail);
        textPassword = (EditText) findViewById(R.id.pass);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    public void login(View view) {
        String mail = textEmail.getText().toString().trim();
        String pass = textPassword.getText().toString().trim();
        //LoginProcess(mail, pass);

        if(mail.isEmpty() || pass.isEmpty()){
            Toast.makeText(LoginAct.this, "incorrect email / password", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Logging in, Please wait");
            progressDialog.show();

            auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();

                    if(task.isSuccessful()){
                        Toast.makeText(LoginAct.this, "logged in", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }else {
                        Toast.makeText(LoginAct.this, "incorrect email or password!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void LoginProcess(String mail, String pass) {
        //Toast.makeText(this, mail, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, pass, Toast.LENGTH_SHORT).show();
        if(mail.equals("user1@email.com") && pass.equals("qwer1234")){
            Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "incorrect email or password!", Toast.LENGTH_SHORT).show();
        }
    }

    public void create(View view) {
        Intent intent = new Intent(this,RegisterAct.class);
        startActivity(intent);
    }
}
