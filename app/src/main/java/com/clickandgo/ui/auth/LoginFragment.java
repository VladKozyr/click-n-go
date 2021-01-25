package com.clickandgo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.clickandgo.SearchActivity;
import com.clickandgo.ui.NavigationHost;
import com.clickandgo.R;
import com.clickandgo.ui.TempFragmentUserAuthed;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emailEditText = view.findViewById(R.id.login_email_edit_text);
        passwordEditText = view.findViewById(R.id.login_password_edit_text);
        MaterialButton btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (isAnyFieldEmpty(email, password)) return;
            signIn(email,password);
        });
    }

    private boolean isAnyFieldEmpty(String email, String password) {
        boolean isAnyFieldEmpty = false;
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

    private void signIn(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getContext(), SearchActivity.class));
                    } else {
                        emailEditText.setText("");
                        passwordEditText.setText("");
                        if (!task.isSuccessful()) handleFirebaseException(task.getException());
                    }
                });
    }

    private void handleFirebaseException(Exception taskException) {
        try{
            throw taskException;
        } catch (FirebaseAuthInvalidCredentialsException e) {
            emailEditText.setError(getString(R.string.incorrect_email_or_password));
            passwordEditText.setError(getString(R.string.incorrect_email_or_password));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
