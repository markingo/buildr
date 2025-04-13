package com.markingo.buildr.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.markingo.buildr.R;
import com.markingo.buildr.model.User;
import com.markingo.buildr.ui.MainActivity;

/**
 * RegisterActivity where users can create a new account
 */
public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputEditText etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        initializeUI();

        // Set up click listeners
        setupListeners();
    }

    private void initializeUI() {
        tilEmail = findViewById(R.id.til_email);
        etEmail = findViewById(R.id.et_email);
        tilPassword = findViewById(R.id.til_password);
        etPassword = findViewById(R.id.et_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> registerUser());
        
        tvLogin.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void registerUser() {
        // Clear previous errors
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);
        
        // Get input values
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";
        
        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.field_required));
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.invalid_email));
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError(getString(R.string.field_required));
            return;
        }
        
        if (password.length() < 6) {
            tilPassword.setError(getString(R.string.weak_password));
            return;
        }
        
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.field_required));
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError(getString(R.string.password_mismatch));
            return;
        }
        
        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);
        
        // Create user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, create user in Firestore
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Create user object
                            User user = new User(firebaseUser.getUid(), email, email.split("@")[0]);
                            
                            // Save user to Firestore
                            firebaseFirestore.collection("users")
                                    .document(firebaseUser.getUid())
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        progressBar.setVisibility(View.GONE);
                                        btnRegister.setEnabled(true);
                                        
                                        Toast.makeText(RegisterActivity.this, 
                                                getString(R.string.register_success), 
                                                Toast.LENGTH_SHORT).show();
                                        
                                        // Navigate to main activity
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressBar.setVisibility(View.GONE);
                                        btnRegister.setEnabled(true);
                                        
                                        Toast.makeText(RegisterActivity.this,
                                                getString(R.string.register_failed, e.getMessage()),
                                                Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        // If sign up fails, display a message to the user.
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setEnabled(true);
                        
                        String errorMessage = "";
                        if (task.getException() != null) {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                errorMessage = "Email already in use. Please use another email or sign in.";
                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                errorMessage = "Password is too weak. Please use a stronger password.";
                            } else {
                                errorMessage = task.getException().getMessage();
                            }
                        }
                        
                        Toast.makeText(RegisterActivity.this, 
                                getString(R.string.register_failed, errorMessage),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
} 