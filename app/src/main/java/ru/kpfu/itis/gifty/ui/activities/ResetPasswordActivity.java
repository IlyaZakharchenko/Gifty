package ru.kpfu.itis.gifty.ui.activities;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static ru.kpfu.itis.gifty.utils.Consts.EMAIL;
import static ru.kpfu.itis.gifty.utils.Consts.EMAIL_REGEX;
import static ru.kpfu.itis.gifty.utils.Keyboard.hide;

import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar.BaseCallback;
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

public class ResetPasswordActivity extends AppCompatActivity {

    private LinearLayout container;
    private TextInputEditText emailEditText;
    private TextInputLayout emailTextInput;
    private ProgressBar progressBar;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initViews();
        initClickListeners();
        initTextListeners();

        String email = getIntent().getStringExtra(EMAIL);
        if (email != null) {
            emailEditText.setText(email);
        }
    }

    private void initClickListeners() {
        resetPasswordButton.setOnClickListener((View v) -> {
            String email = emailEditText.getText().toString();
            hide(container);

            if (TextUtils.isEmpty(email)) {
                emailTextInput.setError(getString(R.string.error_empty_email));
                return;
            }
            if (!email.matches(EMAIL_REGEX)) {
                emailTextInput.setError(getString(R.string.error_wrong_email));
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(container, R.string.reset_success,
                            LENGTH_LONG).addCallback(new BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(final Snackbar transientBottomBar, final int event) {
                            finish();
                        }
                    }).show();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(container, Objects.requireNonNull(task.getException()).getMessage(),
                            LENGTH_LONG);
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
    }

    private void initViews() {
        emailTextInput = findViewById(R.id.it_email);
        emailEditText = findViewById(R.id.et_email);
        resetPasswordButton = findViewById(R.id.btn_reset);
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.container);
    }
}
