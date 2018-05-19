package ru.kpfu.itis.gifty.ui.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.ui.activities.ProfileActivity;

/**
 * Created by Ilya Zakharchenko on 13.05.2018.
 */
public class EditEmailDialogFragment extends DialogFragment implements OnClickListener {
    private static final String EMAIL = "email";
    private Context context;

    private Button okButton;
    private Button cancelButton;
    private TextView titleTextView;
    private EditText infoEditText;

    public static EditEmailDialogFragment newInstance(String info) {
        EditEmailDialogFragment dialog = new EditEmailDialogFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, info);
        dialog.setArguments(args);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_info, container, false);
        okButton = v.findViewById(R.id.btn_ok);
        cancelButton = v.findViewById(R.id.btn_cancel);
        titleTextView = v.findViewById(R.id.tv_title);
        infoEditText = v.findViewById(R.id.et_info);
        Bundle args = getArguments();
        titleTextView.setText(getString(R.string.dialog_title_email));
        infoEditText.setText(args.getString(EMAIL));
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                String info = infoEditText.getText().toString();
                updateInfo(info);
                dismiss();
                break;
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void updateInfo(String email) {
        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ((ProfileActivity) context).updateView(EMAIL, email);
                dismiss();
            }
        });
    }
}
