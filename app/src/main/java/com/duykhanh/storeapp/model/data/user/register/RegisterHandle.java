package com.duykhanh.storeapp.model.data.user.register;

import android.util.Log;

import androidx.annotation.NonNull;

import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.presenter.user.register.RegisterContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterHandle implements RegisterContract.Handle {
    final String TAG = this.getClass().toString();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    String mUserId, mUserEmail;

    public RegisterHandle() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void doRegister(OnDoRegisterListener listener, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    listener.onDoRegisterFinished(firebaseUser);
                } else {
                    listener.onDoRegisterFailure(task.getException());
                }
            }
        });
    }

    @Override
    public void createUser(OnCreateUserListener listener, FirebaseUser firebaseUser) {
        try {
            mUserId = firebaseUser.getUid();
            mUserEmail = firebaseUser.getEmail();
            User user = new User("","",mUserEmail,"","","");
            databaseReference.child(mUserId).setValue(user);
            listener.onCreateUserFinished();
        }
        catch (Exception e){
            listener.onCreateUserFailure(e);
        }
    }

    @Override
    public void autoLogOut() {
        try {
            firebaseAuth.signOut();
        }
        catch (Exception e){
            Log.e(TAG, "autoLogOut: fail", e);
        }
    }
}
