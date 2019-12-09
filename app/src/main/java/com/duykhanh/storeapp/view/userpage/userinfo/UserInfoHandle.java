package com.duykhanh.storeapp.view.userpage.userinfo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.duykhanh.storeapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserInfoHandle implements UserInfoContract.Handle {
    final String TAG = this.getClass().toString();

    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ContentResolver contentResolver;

    public UserInfoHandle(UserInfoContract.View iView) {
        contentResolver = ((Context) iView).getContentResolver();
        sharedPreferences = ((Context) iView).getSharedPreferences("User", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("nguoidungs");
    }

    @Override //Lấy thông tin người dùng, kết hợp chuyển User Image sang Bitmap
    public void getCurrentUser(OnGetCurrentUserListener listener) {
        String userId = sharedPreferences.getString("UserId", "");
        if (!userId.equals("")) {
            //Lấy thông tin người dùng
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d(TAG, "onDataChange: " + user.toString());
                    //Chuyển Info Image sang Bitmap
                    long ONE_MEGABYTE = 1024 * 1024;
                    if (user.getPhoto().equals("")) {
                        listener.onGetCurrenUserFinished(user, null);
                        return;
                    }
                    storageReference.child(user.getPhoto()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Log.d(TAG, "onSuccess: " + bitmap);
                            listener.onGetCurrenUserFinished(user, bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ", e);
                            listener.onGetCurrntUserFailure(e);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onGetCurrntUserFailure(databaseError.toException());
                }
            });
        } else {
            Log.e(TAG, "getUserId: null");
        }
    }

    @Override
    public void changeUserInfo(OnChangeUserInfoListener listener, User user, Uri imageUri) {
        String userId = sharedPreferences.getString("UserId", "");
        if (imageUri != null) {
            String oldImage = user.getPhoto();
            //Đặt tên mới cho ảnh theo định dạng "Time.JPG"
            final String mNewAvatar = System.currentTimeMillis() + "." + getFileExtension(imageUri);
            byte[] image = convertImageToByte(imageUri);
            Log.d(TAG, "changeUserInfo: " + image.toString());
            //Upload Avatar mới của người dùng lên Firestorage
            StorageReference fileReference = storageReference.child(mNewAvatar);
            fileReference.putBytes(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override//Upload ảnh thành công
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Update đường dẫn ảnh mới cho User
                    user.setPhoto(mNewAvatar);
                    databaseReference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override//Update đường dẫn thành công
                        public void onSuccess(Void aVoid) {
                            //Xóa Avatar cũ của người dùng
                            if (oldImage.equals("")) {
                                return;
                            } else {
                                storageReference.child(oldImage).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override //Xóa Avatar cũ thành công
                                    public void onSuccess(Void aVoid) {
                                        listener.onChangeUserInfoFinished();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override //Xóa Avatar cũ thất bại
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onChangeUserInfoFailure(e);
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override //Update đường dẫn thất bại
                        public void onFailure(@NonNull Exception e) {
                            listener.onChangeUserInfoFailure(e);
                        }
                    });
                    Log.d(TAG, "onSuccess: ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override //Upload ãnh thất bại
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: ", e);
                    listener.onChangeUserInfoFailure(e);
                }
            });
        } else {
            databaseReference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override//Update đường dẫn thành công
                public void onSuccess(Void aVoid) {
                    listener.onChangeUserInfoFinished();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override //Update đường dẫn thất bại
                public void onFailure(@NonNull Exception e) {
                    listener.onChangeUserInfoFailure(e);
                }
            });
        }
    }

    private String getFileExtension(Uri uri) { //Lấy đuôi mở rộng của của file ảnh (PNG, JPG,...)
        if (uri == null) {
            return "";
        }
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private byte[] convertImageToByte(Uri imageUri) {
        //Nén ảnh
        byte[] image = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos);
            image = baos.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, "convertImageToByte: ", e);
            ;
        }
        return image;
    }
}
