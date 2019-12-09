package com.duykhanh.storeapp.view.userpage.account.login;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.presenter.user.login.LoginContract;
import com.duykhanh.storeapp.presenter.user.login.LoginPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements LoginContract.View, View.OnClickListener {
    final String TAG = this.getClass().toString();

    View view;
    ProgressBar pbIsLoggingIn;
    EditText etEmail, etPassword;
    Button btnLogIn;

    LoginPresenter presenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initView();
        initComponent();

        btnLogIn.setOnClickListener(this);

        return view;
    }

    @Override
    public void requestLogInFinished() {
        getActivity().finish();
    }

    @Override
    public void requestLogInFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "requestLogInFailure: ", throwable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogIn:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.equals("") || password.equals("")){
                    Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length()<6){
                    Toast.makeText(getContext(), "Mật khẩu gồm ít nhất 6 kí tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.requestLogIn(email, password);
        }
    }

    private void initComponent() {
        presenter = new LoginPresenter(this);
    }

    private void initView() {
        pbIsLoggingIn = view.findViewById(R.id.pbIsLoggingIn);
        etEmail = view.findViewById(R.id.etLogInEmail);
        etPassword = view.findViewById(R.id.etLogInPassword);
        btnLogIn = view.findViewById(R.id.btnLogIn);
    }

    @Override
    public void showProgress() {
        pbIsLoggingIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbIsLoggingIn.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyed();
    }
}
