package ru.kpfu.itis.gifty.ui.activities;

import static android.support.design.widget.Snackbar.LENGTH_LONG;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.User;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.dialogs.EditEmailDialogFragment;
import ru.kpfu.itis.gifty.ui.dialogs.EditNameDialogFragment;

public class ProfileActivity extends BottomNavigationActivity {

    private final String TAG = "ProfileActivity";
    private FirebaseAuth auth;
    private LinearLayout container;
    private ImageView editEmailImageView;
    private ImageView editNameImageView;
    private LinearLayout emailContainer;
    private TextView emailTextView;
    private FirebaseUser firebaseUser;
    private LinearLayout nameContainer;
    private TextView nameTextView;
    private ProgressBar progressBar;
    private UserProvider provider;
    private Button resetPasswordButton;
    private Button signOutButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.i(TAG, "Entering ProfileActivity");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        provider = UserProvider.getInstance();
        if (firebaseUser == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        user = UserProvider.getInstance().getUser();
        initViews();
        fillViews();
        initListeners();
    }

    public void updateView(String viewName, String data) {
        switch (viewName) {
            case "email":
                emailTextView.setText(data);
                break;
            case "name":
                nameTextView.setText(data);
        }
    }

    private void fillViews() {
        navigation.setSelectedItemId(R.id.navigation_profile);
        emailTextView.setText(firebaseUser.getEmail());
        nameTextView.setText(user.getDisplayName());
        editNameImageView.setImageResource(R.drawable.ic_edit);
        editEmailImageView.setImageResource(R.drawable.ic_edit);
    }

    protected void initListeners() {
        navigation.setOnNavigationItemSelectedListener(navigationListener);
        signOutButton.setOnClickListener(v -> {
            auth.signOut();
            provider.clear();
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        resetPasswordButton.setOnClickListener(
                v -> {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(emailTextView.getText().toString()).addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Snackbar.make(container, R.string.reset_success, LENGTH_LONG).show();
                        } else {
                            Snackbar.make(container, Objects.requireNonNull(task.getException()).getMessage(),
                                    LENGTH_LONG);
                        }
                    });
                });
        nameContainer.setOnClickListener(v -> showNameEditDialog(nameTextView.getText().toString()));
        emailContainer.setOnClickListener(v -> showEmailEditDialog(emailTextView.getText().toString()));
    }

    protected void initViews() {
        navigation = findViewById(R.id.navigation);
        nameTextView = findViewById(R.id.tv_name);
        emailTextView = findViewById(R.id.tv_email);
        signOutButton = findViewById(R.id.btn_sign_out);
        resetPasswordButton = findViewById(R.id.btn_reset);
        editNameImageView = findViewById(R.id.iv_edit_name);
        editEmailImageView = findViewById(R.id.iv_edit_email);
        nameContainer = findViewById(R.id.container_name);
        emailContainer = findViewById(R.id.container_email);
        container = findViewById(R.id.container);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void showEmailEditDialog(String data) {
        DialogFragment dialog = EditEmailDialogFragment.newInstance(data);
        dialog.show(getFragmentManager(), TAG);
    }

    private void showNameEditDialog(String data) {
        DialogFragment dialog = EditNameDialogFragment.newInstance(data);
        dialog.show(getFragmentManager(), TAG);
    }
}
