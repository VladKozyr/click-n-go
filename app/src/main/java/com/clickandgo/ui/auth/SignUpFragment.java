package com.clickandgo.ui.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clickandgo.ui.NavigationHost;
import com.clickandgo.R;
import com.clickandgo.ui.TempFragmentUserAuthed;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import static android.content.ContentValues.TAG;

public class SignUpFragment extends Fragment {

    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nameEditText = view.findViewById(R.id.sign_up_name_edit_text);
        emailEditText = view.findViewById(R.id.sign_up_email_edit_text);
        passwordEditText = view.findViewById(R.id.sign_up_password_edit_text);
        MaterialButton btnLogin = view.findViewById(R.id.btn_sign_up);

        btnLogin.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (isAnyFieldEmpty(name, email, password)) return;
            singUp(email, password);
        });
    }

    private void singUp(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        ((NavigationHost) getActivity()).navigateTo(new TempFragmentUserAuthed(), false); // Navigate to the next Fragment
                    } else {
                        if (!task.isSuccessful()) handleFirebaseException(task.getException());
                    }
                });
    }

    private boolean isAnyFieldEmpty(String name, String email, String password) {
        boolean isAnyFieldEmpty = false;
        if (name.isEmpty()) {
            nameEditText.setError(getString(R.string.field_cannot_be_empty));
            isAnyFieldEmpty = true;
        }
        if (email.isEmpty()) {
            emailEditText.setError(getString(R.string.field_cannot_be_empty));
            isAnyFieldEmpty = true;
        }
        if (password.isEmpty()) {
            passwordEditText.setError(getString(R.string.field_cannot_be_empty));
            isAnyFieldEmpty = true;
        }
        return isAnyFieldEmpty;
    }

    private void handleFirebaseException(Exception taskException) {
        try {
            throw taskException;
        } catch (FirebaseAuthWeakPasswordException e) {
            passwordEditText.setError(getString(R.string.password_is_less_than_6_characters));
            passwordEditText.setText("");
            passwordEditText.requestFocus();
        } catch (FirebaseAuthUserCollisionException e) {
            emailEditText.setError(getString(R.string.email_is_already_in_use));
            emailEditText.setText("");
            emailEditText.requestFocus();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            emailEditText.setError(getString(R.string.email_address_is_badly_formatted));
            emailEditText.setText("");
            emailEditText.requestFocus();
        } catch (Exception e) {
            nameEditText.setText("");
            emailEditText.setText("");
            passwordEditText.setText("");
            Log.e(TAG, e.getMessage());
        }
    }

}
