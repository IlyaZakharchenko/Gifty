package ru.kpfu.itis.gifty.ui.activities;

import static ru.kpfu.itis.gifty.utils.Consts.EMAIL_REGEX;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.providers.UserProvider;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private LinearLayout container;
    private TextInputEditText emailEditText;
    private TextInputLayout emailTextInput;
    private TextInputEditText passwordEditText;
    private TextInputLayout passwordTextInput;
    private ProgressBar progressBar;
    private Button signUpButton;
    private TextInputEditText nameEditText;
    private TextInputLayout nameTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        initViews();
        initClickListeners();
        initTextListeners();
    }

    private void initClickListeners() {
        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String name = nameEditText.getText().toString();
            hide(container);
            if (TextUtils.isEmpty(email)) {
                emailTextInput.setError(getString(R.string.error_empty_email));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordTextInput.setError(getString(R.string.error_empty_password));
                return;
            }
            if (TextUtils.isEmpty(name)) {
                nameTextInput.setError(getString(R.string.error_empty_fullname));
            }
            if (password.length() < 6) {
                passwordTextInput.setError(getString(R.string.error_short_password));
                return;
            }
            if (!email.matches(EMAIL_REGEX)) {
                emailTextInput.setError(getString(R.string.error_wrong_email));
                return;
            }

            container.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    task -> {

                        if (task.isSuccessful()) {
                            String uid = task.getResult().getUser().getUid();
                            UserProvider.getInstance().createUser(new User(uid, name));
                            Intent intent = new Intent(this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            container.setVisibility(View.VISIBLE);
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

        nameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(final Editable s) {
            }

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                nameTextInput.setError(null);
            }
        });
    }

    private void initViews() {
        signUpButton = findViewById(R.id.btn_sign_up);
        passwordEditText = findViewById(R.id.et_password);
        emailEditText = findViewById(R.id.et_email);
        nameEditText = findViewById(R.id.et_name);
        emailTextInput = findViewById(R.id.it_email);
        passwordTextInput = findViewById(R.id.it_password);
        nameTextInput = findViewById(R.id.it_name);
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.container);
    }
}
