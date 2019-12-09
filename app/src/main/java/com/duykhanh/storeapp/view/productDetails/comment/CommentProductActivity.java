package com.duykhanh.storeapp.view.productDetails.comment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.comment.CommentImageAdapter;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.presenter.comment.CommentContract;
import com.duykhanh.storeapp.presenter.comment.CommentPresenter;
import com.duykhanh.storeapp.utils.Formater;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.duykhanh.storeapp.utils.Constants.*;

public class CommentProductActivity extends AppCompatActivity implements View.OnClickListener, CommentproductItemListener,
        CommentContract.View {

    private static final String TAG = CommentProductActivity.class.getSimpleName();
    ImageView img_commentProduct;
    TextView tv_nameProduct, tv_contentRatingBar;
    RatingBar rtb_comment;
    ImageButton img_camera_comment_one;
    LinearLayout ln_image_comment;

    // Hiển thị danh sách hình ảnh người dùng đã chọn
    RecyclerView rcl_image_comment;
    TextInputEditText ed_title_comment, ed_write_comment;

    Button btn_send_comment;

    Formater formater;

    List<Uri> path;
    int maxCount = 5;
    int countRatingbar = 0;

    CommentImageAdapter imageAdapter;
    LinearLayoutManager layoutManager;

    Product product;

    List<String> realPath;
    List<MultipartBody.Part> parts;
    Map<String, RequestBody> map;
    int countClick = 0;

    CommentContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_product);
        setTitle("Bình luận");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Ánh xạ view
        initUI();

        registerListener();

        // Hàm lắng nghe sự kiện khi người dùng chọn sao đánh giá sản phẩm
        onClickRatingBar();

        getDataIntent();
    }

    private void getDataIntent() {
        formater = new Formater();

        Intent iProduct = getIntent();

        product = (Product) iProduct.getSerializableExtra(KEY_COMMENT_PRODUCT);

        String url = null;
        try {
            url = formater.formatImageLink(product.getImg().get(0));
        } catch (Exception e) {
            Log.d("Error", "onBindViewHolder: " + e);
        }

        Glide.with(this).load(url).apply(new RequestOptions().placeholder(R.drawable.noimage).error(R.drawable.noimage))
                .into(img_commentProduct);

        tv_nameProduct.setText(product.getNameproduct());
    }

    private void initUI() {
        img_commentProduct = findViewById(R.id.img_commentProduct);
        tv_nameProduct = findViewById(R.id.tv_nameProduct);

        tv_contentRatingBar = findViewById(R.id.tv_contentRatingBar);
        rtb_comment = findViewById(R.id.rtb_comment);
        img_camera_comment_one = findViewById(R.id.img_camera_comment_one);
        rcl_image_comment = findViewById(R.id.rcl_image_comment);
        ed_title_comment = findViewById(R.id.ed_title_comment);
        ed_write_comment = findViewById(R.id.ed_write_comment);

        ln_image_comment = findViewById(R.id.ln_image_comment);
        btn_send_comment = findViewById(R.id.btn_send_comment);

        path = new ArrayList<>(5);
        realPath = new ArrayList<>(5);
        presenter = new CommentPresenter(this);
        parts = new ArrayList<>(5);
    }

    private void registerListener() {
        btn_send_comment.setOnClickListener(this);
        img_commentProduct.setOnClickListener(this);
        img_camera_comment_one.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_comment:
                if (validateFormContent()) {
                    onRequestDataFormServer();
                }
                break;
            case R.id.img_commentProduct:

                break;
            case R.id.img_camera_comment_one:
                onImagePickerMultiple();
                break;
        }
    }

    //TODO: Chọn nhiều ảnh bình luận
    private void onImagePickerMultiple() {
        FishBun.with(CommentProductActivity.this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(maxCount - path.size())
                .setMinCount(1)
                .setPickerSpanCount(5)
                .setActionBarColor(Color.parseColor("#1E88E5"), Color.parseColor("#1565C0"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .setAlbumSpanCount(2, 3)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .exceptGif(true)
                .setReachLimitAutomaticClose(true)
                .setAllViewTitle("Tất cả")
                .setMenuAllDoneText("All Done")
                .setActionBarTitle("Chọn ảnh")
                .textOnNothingSelected("Vui lòng chọn ảnh!")
                .startAlbum();
    }

    private void onRequestDataFormServer() {
        for (int i = 0; i < path.size(); i++) {
            realPath.add(i, getRealPathFromURI(path.get(i)));
        }
        getRealPath();
        getDataForm();
        presenter.requestDataFormServer(parts, map);
        formatPathImage();
    }

    private void getDataForm() {
        DateFormat dateFormatter = new SimpleDateFormat("mm/dd/yyyy");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);
        Log.d("DATEE", "getDataForm: " + s);
        RequestBody content = RequestBody.create(MediaType.parse("text/plain"), ed_write_comment.getText().toString());
        RequestBody idp = RequestBody.create(MediaType.parse("text/plain"), product.getId());
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), "11/11/2019");
        RequestBody point = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(countRatingbar));
        RequestBody idu = RequestBody.create(MediaType.parse("text/plain"), getUserId());
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), ed_title_comment.getText().toString());
        map = new HashMap<>();
        map.put("content", content);
        map.put("idp", idp);
        map.put("date", date);
        map.put("point", point);
        map.put("idu", idu);
        map.put("title", title);
    }

    private String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return "";
    }

    @SuppressLint("ResourceAsColor")
    private void onClickRatingBar() {
        rtb_comment.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                /*
                 * Show ra mức độ hài lòng sản phẩm khi người dùng chọn số lượng sao
                 */
                tv_contentRatingBar.setText(getString(R.string.first_star));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        tv_contentRatingBar.setText(getString(R.string.star_one));
                        countRatingbar = 1;
                        break;
                    case 2:
                        tv_contentRatingBar.setText(getString(R.string.star_two));
                        countRatingbar = 2;
                        break;
                    case 3:
                        tv_contentRatingBar.setText(getString(R.string.star_three));
                        countRatingbar = 3;
                        break;
                    case 4:
                        tv_contentRatingBar.setText(getString(R.string.star_four));
                        countRatingbar = 4;
                        break;
                    case 5:
                        tv_contentRatingBar.setText(getString(R.string.star_five));
                        countRatingbar = 5;
                        break;
                    default:
                        countRatingbar = 0;
                        tv_contentRatingBar.setText(getString(R.string.first_star));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        countClick++;
        switch (requestCode) {
            case Define
                    .ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (path.size() <= 5) {
                        for (int i = 0; i < data.getParcelableArrayListExtra(Define.INTENT_PATH).size(); i++) {
                            path.add(i, (Uri) data.getParcelableArrayListExtra(Define.INTENT_PATH).get(i));
                        }
                        if (path.size() == 5) {
                            ln_image_comment.setVisibility(View.GONE);
                        }
                    }

                    if (path != null) {
                        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                        rcl_image_comment.setLayoutManager(layoutManager);
                        rcl_image_comment.setItemAnimator(new DefaultItemAnimator());
                        imageAdapter = new CommentImageAdapter(path, this, this);
                        rcl_image_comment.setAdapter(imageAdapter);
                    }
                    break;
                }
        }
    }//not

    //TODO Lấy đường dẫn thực tế tù Uri
    public String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null,
                null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();

                result = "";
            }
            cursor.close();
        }
        return result;
    }//not

    // TODO Xử lý đường dẫn ảnh và gửi lên server
    private void getRealPath() {//here
        for (int i = 0; i < realPath.size(); i++) {
            File file = new File(realPath.get(i));
            String file_path = file.getAbsolutePath();

            /*
             * Thay đổi tên hình ảnh mỗi lần gửi lên server
             */
//            String[] arrayNameFile = file_path.split("\\.");
//            file_path = arrayNameFile[0] + "." + arrayNameFile[1] + "." + arrayNameFile[2] + System.currentTimeMillis() + "." + arrayNameFile[3];
//            Log.d(TAG, "getRealPath: " + file_path);

            /*
             * Khai báo kiểu dữ liệu và đường dẫn ảnh khi gửi lên server
             */

            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file_path, requestBody);
            parts.add(i, body);
        }
    }

    @Override
    public void onCliCkCommentProductitem(int position) {
        path.remove(position);
        formatPathImage();
        Toast.makeText(this, "" + path.size(), Toast.LENGTH_SHORT).show();
        imageAdapter.notifyDataSetChanged();
        ln_image_comment.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendDataRecyclerViewComment(List<Comment> commentList) {
        //not code
    }

    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onFailed() {
        Toast.makeText(this, "Đã có lỗi xảy ra. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Throwable t) {
        finish();
        Toast.makeText(this, "Vui lòng kiểm tra lại đường truyền. Hoặc liên hệ với nhà cung cấp dịch vụ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailedComment() {
        //not code
    }

    @Override
    public void onFailureComment(Throwable t) {
        // not code
    }

    public void formatPathImage() {//here
        for (int i = 0; i < parts.size(); i++) {
            parts.remove(i);

        }
        for (int j = 0; j < realPath.size(); j++) {
            realPath.remove(j);
        }
    }

    public boolean validateFormContent() {
        if (ed_write_comment.getText().toString().equals("")) {
            Toast.makeText(this, "Vui lòng nhập nội dung bài viết", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
