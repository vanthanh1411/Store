package com.duykhanh.storeapp.model.data.user.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.duykhanh.storeapp.presenter.user.login.LoginContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class LoginHandle implements LoginContract.Handle {
    final String TAG = this.getClass().toString();

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String userId;

    public LoginHandle(LoginContract.View iView) {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        sharedPreferences = iView.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void loggingIn(OnLoggingInListener listener, String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    listener.onLoggingInFailure(task.getException());
                    return;
                }
                userId = firebaseAuth.getCurrentUser().getUid();
                listener.onLoggingInFinished(userId);
            }
        });
    }

    @Override
    public void getTokenId(OnGetTokenIdListener listener, String userId) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String tokenId = task.getResult().getToken();
                        listener.onGetTokenIdFinished(userId, tokenId);
                    }
                });

    }

    @Override
    public void putTokenIdToUser(OnPutTokenIdToUserListener listener, String userId, String tokenId) {
        try {
            databaseReference.child(userId).child("tokenID").setValue(tokenId);
            listener.onPutTokenIdToUserFinished();
        } catch (Exception e) {
            listener.onPutTokenIdToUserFailure(e);
        }
    }

    @Override
    public void storeUserId(OnStoreUserIdListener listener, String userId) {
        try{
            editor.putString("UserId",userId);
            editor.apply();
            listener.onStoreUserIdFinished();
        }
        catch (Exception e){
            listener.onStoreUserIdFailure();
        }
    }
}
