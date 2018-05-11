package ru.kpfu.itis.gifty.activity;

import static ru.kpfu.itis.gifty.utils.Consts.EMAIL_REGEX;
import static ru.kpfu.itis.gifty.utils.Keyboard.hide;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;
import ru.kpfu.itis.gifty.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private LinearLayout container;
    private EditText emailEditText;
    private TextInputLayout emailTextInput;
    private EditText passwordEditText;
    private TextInputLayout passwordTextInput;
    private ProgressBar progressBar;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        initViews();
        initClickListeners();
        initTextListeners();
        initKeyListeners();
    }

    private void initClickListeners() {
        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            hide(container);
            if (TextUtils.isEmpty(email)) {
                emailTextInput.setError(getString(R.string.error_empty_email));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordTextInput.setError(getString(R.string.error_empty_password));
                return;
            }
            if (password.length() < 6) {
                passwordTextInput.setError(getString(R.string.error_short_password));
                return;
            }
            if (!email.matches(EMAIL_REGEX)) {
                emailTextInput.setError(getString(R.string.error_wrong_email));
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    task -> {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            startMainActivity();
                        } else {
                            Snackbar.make(
                                    container,
                                    Objects.requireNonNull(task.getException()).getMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void initTextListeners() {
        emailEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable s) {
            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                emailTextInput.setError(null);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable s) {
            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                passwordTextInput.setError(null);
            }
        });
    }

    private void initKeyListeners() {
        passwordEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                signUpButton.performClick();
            }

            return false;
        });
    }

    private void initViews() {
        signUpButton = findViewById(R.id.btn_sign_up);
        passwordEditText = findViewById(R.id.et_password);
        emailEditText = findViewById(R.id.et_email);
        emailTextInput = findViewById(R.id.it_email);
        passwordTextInput = findViewById(R.id.it_password);
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.container);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
