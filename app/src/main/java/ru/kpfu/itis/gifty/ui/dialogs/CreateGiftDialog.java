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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ru.kpfu.itis.gifty.R;
import ru.kpfu.itis.gifty.model.entities.Gift;
import ru.kpfu.itis.gifty.model.entities.Group;
import ru.kpfu.itis.gifty.model.providers.UserProvider;
import ru.kpfu.itis.gifty.ui.fragments.GiftListFragment;

/**
 * Created by Ilya Zakharchenko on 28.05.2018.
 */
public class CreateGiftDialog extends DialogFragment {

    private Button buttonCancel;
    private Button buttonOk;
    private TextInputEditText nameEditText;
    private TextInputLayout nameInputLayout;
    private TextView titleTextView;

    public static CreateGiftDialog newInstance() {

        Bundle args = new Bundle();

        CreateGiftDialog fragment = new CreateGiftDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_create_group, container, false);
        initViews(v);
        initListeners();
        return v;
    }

    private void initListeners() {
        buttonOk.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            if (name.isEmpty()) {
                nameInputLayout.setError(getString(R.string.error_empty_gift));
                return;
            }
            for (Group group : UserProvider.getInstance().getUser().getGroupList()) {
                if (group.getName().equals(name)) {
                    nameInputLayout.setError(getString(R.string.error_gift_exists));
                    return;
                }
            }
            UserProvider.getInstance().addGift(new Gift(name));
            ((GiftListFragment) getTargetFragment()).updateList();
            dismiss();
        });

        buttonCancel.setOnClickListener(v -> dismiss());

        nameEditText.addTextChangedListener(new TextWatcher() {
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
        buttonOk = v.findViewById(R.id.btn_ok);
        buttonCancel = v.findViewById(R.id.btn_cancel);
        nameInputLayout = v.findViewById(R.id.ti_name);
        nameEditText = v.findViewById(R.id.et_name);
        titleTextView = v.findViewById(R.id.tv_title);
        titleTextView.setText(R.string.create_gift);
    }

}
