package ru.kpfu.itis.gifty.activity;

import static ru.kpfu.itis.gifty.utils.Keyboard.hide;
import static ru.kpfu.itis.gifty.utils.Consts.*;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.util.ui.SupportVectorDrawablesButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.kpfu.itis.gifty.R;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private LinearLayout container;
    private EditText emailEditText;
    private TextInputLayout emailTextInput;
    private EditText passwordEditText;
    private TextInputLayout passwordTextInput;
    private SupportVectorDrawablesButton phoneButton;
    private ProgressBar progressBar;
    private TextView resetPassTV;
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();
        initClickListeners();
        initTextListeners();
        initKeyListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
//            startMainActivity();
        }
    }

    private void initClickListeners() {
        resetPassTV.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
            intent.putExtra(EMAIL, email);
            startActivity(intent);
        });

        phoneButton.setOnClickListener(v -> {
            List<IdpConfig> providers = Collections.singletonList(
                    new IdpConfig.PhoneBuilder().build()
            );

            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        });

        signUpButton.setOnClickListener(v ->
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));

        signInButton.setOnClickListener(v -> {
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

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
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
                signInButton.performClick();
            }

            return false;
        });
    }

    private void initViews() {
        signInButton = findViewById(R.id.btn_sign_in);
        signUpButton = findViewById(R.id.btn_sign_up);
        phoneButton = findViewById(R.id.btn_phone);
        passwordEditText = findViewById(R.id.et_password);
        emailEditText = findViewById(R.id.et_email);
        emailTextInput = findViewById(R.id.it_email);
        passwordTextInput = findViewById(R.id.it_password);
        resetPassTV = findViewById(R.id.tv_reset_password);
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.container);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
