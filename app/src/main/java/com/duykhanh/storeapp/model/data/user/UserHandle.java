package com.duykhanh.storeapp.model.data.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.presenter.user.UserContract;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserHandle implements UserContract.Handle {
    final String TAG = this.getClass().toString();
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    public UserHandle(UserContract.View iView) {
        sharedPreferences = (iView.getContext()).getSharedPreferences("User", Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("nguoidungs");
    }

    @Override//Lấy User hiện tại
    public void getCurrentUser(OnGetCurrentUserListener listener) {
        String userId = sharedPreferences.getString("UserId", "");
        try {
            if (!userId.equals("")) {
                Log.d(TAG, "getUserId: " + userId);
                //Lấy thông tin người dùng
                databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Log.d(TAG, "getUserId: " + user);
                        //Chuyển Info Image sang Bitmap
                        long ONE_MEGABYTE = 1024 * 1024;
                        if (user.getPhoto().equals("")) {
                            listener.onGetCurrentUserFinished(user, null);
                            return;
                        }
                        storageReference.child(user.getPhoto()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                Log.d(TAG, "onSuccess: " + bitmap.toString());
                                listener.onGetCurrentUserFinished(user, bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: ", e);
                                listener.onGetCurrentUserFailure(e);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onGetCurrentUserFailure(databaseError.toException());
                    }
                });
            } else {
                listener.onGetCurrentUserFinished(null, null);
            }
        } catch (Exception e) {
            listener.onGetCurrentUserFailure(e);
        }
    }

    @Override
    public void logOut(OnLogOutListener listener) {
        try {
            firebaseAuth.signOut();
            listener.onLogoutFinished();
        } catch (Exception e) {
            listener.onLogOutFailure(e);
        }
    }

    @Override
    public void removeUserId(OnRemoveUserIdListener listener) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("UserId").apply();
            listener.onRemoveUserIdFinished();
        } catch (Exception e) {
            listener.onRemoveUserIdFailure(e);
        }
    }
}
