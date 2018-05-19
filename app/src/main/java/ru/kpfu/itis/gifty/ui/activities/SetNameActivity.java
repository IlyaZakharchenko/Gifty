package ru.kpfu.itis.gifty.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.providers.UserProvider;

public class SetNameActivity extends AppCompatActivity {

    private Button buttonSignUp;
    private LinearLayout container;
    private TextInputEditText nameEditText;
    private TextInputLayout nameTextInput;
    private ProgressBar progressBar;
    private UserProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        provider = UserProvider.getInstance();
        initViews();
        initClickListeners();
        initTextListeners();
    }

    @Override
    public void onBackPressed() {
        Snackbar.make(container, getString(R.string.snackbar_enter_name), Snackbar.LENGTH_LONG).show();
    }

    private void initClickListeners() {
        buttonSignUp.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            if (name.isEmpty()) {
                nameTextInput.setError(getString(R.string.error_empty_fullname));
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            container.setVisibility(View.GONE);
            provider.provideUser().addOnCompleteListener((user, isSuccessful) -> {
                provider.updateName(name);
                setResult(RESULT_OK);
                finish();
            });
        });
    }

    private void initTextListeners() {
        nameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                nameTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
    }

    private void initViews() {
        nameTextInput = findViewById(R.id.it_name);
        container = findViewById(R.id.container);
        nameEditText = findViewById(R.id.et_name);
        buttonSignUp = findViewById(R.id.btn_sign_up);
        progressBar = findViewById(R.id.progress_bar);
    }
}
