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
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.fragments.ProfileFragment;

/**
 * Created by Ilya Zakharchenko on 13.05.2018.
 */
public class EditNameDialog extends DialogFragment implements OnClickListener {

    private static final String NAME = "name";
    private Button cancelButton;
    private TextInputEditText infoEditText;
    private Button okButton;
    private TextView titleTextView;
    private TextInputLayout nameInputLayout;

    public static EditNameDialog newInstance(String info) {
        EditNameDialog dialog = new EditNameDialog();
        Bundle args = new Bundle();
        args.putString(NAME, info);
        dialog.setArguments(args);

        return dialog;
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

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                String name = infoEditText.getText().toString();
                if (name.isEmpty()) {
                    nameInputLayout.setError(getString(R.string.error_empty_fullname));
                    return;
                }
                UserProvider.getInstance().updateName(name);
                ((ProfileFragment) getTargetFragment()).updateView(NAME, name);
                dismiss();
                break;
        }
    }

    private void fillViews() {
        Bundle args = getArguments();
        infoEditText.setText(args.getString(NAME));
        titleTextView.setText(getString(R.string.dialog_title_name));
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
                nameInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initViews(View v) {
        okButton = v.findViewById(R.id.btn_ok);
        cancelButton = v.findViewById(R.id.btn_cancel);
        titleTextView = v.findViewById(R.id.tv_title);
        infoEditText = v.findViewById(R.id.et_name);
        nameInputLayout = v.findViewById(R.id.ti_name);
    }
}
