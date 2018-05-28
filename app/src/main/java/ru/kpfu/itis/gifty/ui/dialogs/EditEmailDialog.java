package ru.kpfu.itis.gifty.ui.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.ui.fragments.ProfileFragment;
import ru.kpfu.itis.gifty.utils.Consts;

/**
 * Created by Ilya Zakharchenko on 13.05.2018.
 */
public class EditEmailDialog extends DialogFragment implements OnClickListener {

    private static final String EMAIL = "email";
    private Button cancelButton;
    private TextInputEditText infoEditText;
    private Button okButton;
    private TextView titleTextView;
    private TextInputLayout emailInputLayout;

    public static EditEmailDialog newInstance(String info) {
        EditEmailDialog dialog = new EditEmailDialog();
        Bundle args = new Bundle();
        args.putString(EMAIL, info);
        dialog.setArguments(args);
        return dialog;
    }

    private void initViews(View v) {
        okButton = v.findViewById(R.id.btn_ok);
        cancelButton = v.findViewById(R.id.btn_cancel);
        titleTextView = v.findViewById(R.id.tv_title);
        titleTextView.setText(getString(R.string.dialog_title_email));
        infoEditText = v.findViewById(R.id.et_name);
        emailInputLayout = v.findViewById(R.id.ti_name);
    }

    private void initListeners() {
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        infoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_create_group, container, false);
        initViews(v);
        initListeners();
        fillViews();
        return v;
    }

    private void fillViews() {
        Bundle args = getArguments();
        titleTextView.setText(getString(R.string.dialog_title_email));
        infoEditText.setText(args.getString(EMAIL));
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                String info = infoEditText.getText().toString();
                if (info.isEmpty()) {
                    emailInputLayout.setError(getString(R.string.error_empty_email));
                    return;
                }
                if (!info.matches(Consts.EMAIL_REGEX)) {
                    emailInputLayout.setError(getString(R.string.error_wrong_email));
                    return;
                }
                updateInfo(info);
                break;
        }
    }

    private void updateInfo(String email) {
        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ((ProfileFragment) getTargetFragment()).updateView(EMAIL, email);
                dismiss();
            }
            else {
                emailInputLayout.setError(getString(R.string.error_email_occupied));
            }
        });
    }
}
