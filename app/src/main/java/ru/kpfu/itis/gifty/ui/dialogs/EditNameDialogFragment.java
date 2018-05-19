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
import android.widget.ProgressBar;
import android.widget.TextView;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.activities.ProfileActivity;

/**
 * Created by Ilya Zakharchenko on 13.05.2018.
 */
public class EditNameDialogFragment extends DialogFragment implements OnClickListener {
    private static final String NAME = "name";
    private Context context;

    private Button okButton;
    private Button cancelButton;
    private TextView titleTextView;
    private EditText infoEditText;
    private ProgressBar progressBar;

    public static EditNameDialogFragment newInstance(String info) {
        EditNameDialogFragment dialog = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString(NAME, info);
        dialog.setArguments(args);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
            final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_edit_info, container, false);
        initViews(v);
        initListeners();
        fillViews();

        return v;
    }

    private void initViews(View v) {
        okButton = v.findViewById(R.id.btn_ok);
        cancelButton = v.findViewById(R.id.btn_cancel);
        titleTextView = v.findViewById(R.id.tv_title);
        infoEditText = v.findViewById(R.id.et_info);
        progressBar = v.findViewById(R.id.progress_bar);
    }

    private void fillViews() {
        Bundle args = getArguments();
        infoEditText.setText(args.getString(NAME));
        titleTextView.setText(getString(R.string.dialog_title_name));
    }

    private void initListeners() {
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                String name = infoEditText.getText().toString();
                UserProvider.getInstance().updateName(name);
                ((ProfileActivity) context).updateView(NAME, name);
                dismiss();
                break;
        }
    }
}
