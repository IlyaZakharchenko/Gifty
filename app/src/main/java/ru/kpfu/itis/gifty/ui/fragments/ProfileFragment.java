package ru.kpfu.itis.gifty.ui.fragments;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static ru.kpfu.itis.gifty.utils.Consts.RC_EMAIL;
import static ru.kpfu.itis.gifty.utils.Consts.RC_NAME;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import ru.kpfu.itis.gifty.ui.activities.SignInActivity;
import ru.kpfu.itis.gifty.ui.dialogs.EditEmailDialog;
import ru.kpfu.itis.gifty.ui.dialogs.EditNameDialog;

public class ProfileFragment extends Fragment {

    private final String TAG = "ProfileFragment";
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

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Entering ProfileFragment");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        provider = UserProvider.getInstance();
        if (firebaseUser == null) {
            startActivity(new Intent(getActivity(), SignInActivity.class));
            getActivity().finish();
        }
        user = UserProvider.getInstance().getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(v);
        initListeners();
        fillViews();
        return v;
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

    private void initListeners() {
        signOutButton.setOnClickListener(v -> {
            auth.signOut();
            provider.clear();
            Intent intent = new Intent(getActivity(), SignInActivity.class);
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

    private void initViews(View v) {
        nameTextView = v.findViewById(R.id.tv_name);
        emailTextView = v.findViewById(R.id.tv_email);
        signOutButton = v.findViewById(R.id.btn_sign_out);
        resetPasswordButton = v.findViewById(R.id.btn_reset);
        editNameImageView = v.findViewById(R.id.iv_edit_name);
        editEmailImageView = v.findViewById(R.id.iv_edit_email);
        nameContainer = v.findViewById(R.id.container_name);
        emailContainer = v.findViewById(R.id.container_email);
        container = v.findViewById(R.id.container);
        progressBar = v.findViewById(R.id.progress_bar);
    }

    private void fillViews() {
        if (firebaseUser.getEmail() != null && !firebaseUser.getEmail().isEmpty()) {
            emailTextView.setText(firebaseUser.getEmail());
        }
        else emailContainer.setVisibility(View.GONE);
        nameTextView.setText(user.getDisplayName());
        editNameImageView.setImageResource(R.drawable.ic_edit);
        editEmailImageView.setImageResource(R.drawable.ic_edit);
    }

    private void showEmailEditDialog(String data) {
        DialogFragment dialog = EditEmailDialog.newInstance(data);
        dialog.setTargetFragment(this, RC_EMAIL);
        dialog.show(getFragmentManager(), TAG);
    }

    private void showNameEditDialog(String data) {
        DialogFragment dialog = EditNameDialog.newInstance(data);
        dialog.setTargetFragment(this, RC_NAME);
        dialog.show(getFragmentManager(), TAG);
    }
}
