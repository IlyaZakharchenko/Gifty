package ru.kpfu.itis.gifty.ui.activities;

import static ru.kpfu.itis.gifty.utils.Consts.EMAIL;
import static ru.kpfu.itis.gifty.utils.Consts.EMAIL_REGEX;
import static ru.kpfu.itis.gifty.utils.Consts.RC_NAME;
import static ru.kpfu.itis.gifty.utils.Consts.RC_PHONE;
import static ru.kpfu.itis.gifty.utils.Keyboard.hide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import ru.kpfu.itis.gifty.model.providers.UserProvider;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    private FirebaseAuth auth;

    private LinearLayout container;
    private TextInputEditText emailEditText;
    private TextInputLayout emailTextInput;
    private TextInputEditText passwordEditText;
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
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            progressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
            UserProvider.getInstance().provideUser().addOnCompleteListener(
                    user -> startGiftListActivity());
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_PHONE:
                    startActivityForResult(new Intent(this, SetNameActivity.class), RC_NAME);
                    break;
                case RC_NAME:
                    progressBar.setVisibility(View.VISIBLE);
                    container.setVisibility(View.GONE);
                    UserProvider.getInstance().provideUser().addOnCompleteListener(
                            user -> {
                                progressBar.setVisibility(View.GONE);
                                container.setVisibility(View.VISIBLE);
                                startGiftListActivity();
                            });
                    break;
            }
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
                    RC_PHONE);
        });

        signUpButton.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            hide(getCurrentFocus());

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
            container.setVisibility(View.GONE);
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    task -> {

                        if (task.isSuccessful()) {
                            UserProvider.getInstance().provideUser().addOnCompleteListener(
                                    user -> startGiftListActivity());
                        } else {
                            Snackbar.make(
                                    container,
                                    Objects.requireNonNull(task.getException()).getMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
                        }
                    });
        });
    }

    private void initTextListeners() {
        emailEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }            @Override
            public void afterTextChanged(final Editable s) {
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

    private void startGiftListActivity() {
        Intent intent = new Intent(this, GiftListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
