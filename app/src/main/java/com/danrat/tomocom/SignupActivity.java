package com.danrat.tomocom;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.danrat.tomocom.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore fireStore;
    private EditText emailET;
    private EditText passwordET;
    private EditText usernameET;
    private EditText ageET;
    private ProgressBar progressBar;

    private String userID;
    private String email;
    private String password;
    private String username;
    private int age;

    private User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        usernameET = findViewById(R.id.username);
        ageET = findViewById(R.id.age);
        progressBar = findViewById(R.id.signUpProgressBar);
        Button registerButton = findViewById(R.id.registerButton);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailET.getText().toString().trim();
                password = passwordET.getText().toString().trim();
                username = usernameET.getText().toString().trim();
                age = Integer.parseInt(ageET.getText().toString().trim());

                newUser = new User(email,username,age);

                if (!isValidEmail(email)) {
                    emailET.setError("Email jest niepoprawny!");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    usernameET.setError("Nazwa użytkownika jest wymagana!");
                    return;
                }

                if (isAgeEmpty(age)) {
                    ageET.setError("Wiek jest wymagany!");
                    return;
                }

                if (age<13) {
                    ageET.setError("Musisz mieć minimum 13 lat!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordET.setError("Hasło jest wymagane!");
                    return;
                }

                if (password.length() < 8) {
                    passwordET.setError("Hasło musi składać się z Ośmiu znaków!");
                    return;
                }

                if (!password.matches(".*[A-Z].*")) {
                    passwordET.setError("Hasło nie posiada wielkiej litery!");
                    return;
                }

                if (!password.matches(".*[0-9].*")) {
                    passwordET.setError("Hasło nie posiada cyfry!");
                    return;
                }

                if (!password.matches(".*[!@$%^*+#].*")) {
                    passwordET.setError("Hasło nie posiada znaku specjalnego!");
                    return;
                }

                if (!password.matches(".*[a-zA-Z0-9!@$%^*+#].*")) {
                    passwordET.setError("Hasło posiada niedozwolony znak!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                createAccount(email,password);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    private void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Utworzono konto",Toast.LENGTH_SHORT).show();
                            userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            DocumentReference documentReference = fireStore.collection("users").document(userID);
                            newUser.setUid(userID);
                            documentReference.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user created with id: "+userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "onFailure: " + e);
                                }
                            });
                            Log.d(TAG, "createUserWithEmail:success");
                            startActivity(new Intent(getApplicationContext(), InterestSelectorActivity.class));
                            finish();
                        }
                        else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Error! : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isAgeEmpty (Integer num) {
        return num == null || num == 0;
    }

}