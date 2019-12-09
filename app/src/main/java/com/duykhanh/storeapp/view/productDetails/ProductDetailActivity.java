package com.duykhanh.storeapp.view.productDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.comment.CommentsAdapter;
import com.duykhanh.storeapp.adapter.slide.SlideAdapter;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.User;
import com.duykhanh.storeapp.presenter.productdetail.ProductDetailContract;
import com.duykhanh.storeapp.presenter.productdetail.ProductDetailPresenter;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.MainActivity;
import com.duykhanh.storeapp.view.categorypage.CategoryListProductActivity;
import com.duykhanh.storeapp.view.order.OrderActivity;
import com.duykhanh.storeapp.view.productDetails.comment.CommentProductActivity;
import com.duykhanh.storeapp.view.productDetails.comment.MoreCommentProductActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_COMMENT_PRODUCT;
import static com.duykhanh.storeapp.utils.Constants.KEY_DATA_CATEGORY_TO_DETAIL_PRODUCT;
import static com.duykhanh.storeapp.utils.Constants.KEY_DATA_HOME_TO_DETAIL_PRODUCT;
import static com.duykhanh.storeapp.utils.Constants.KEY_ITEM_CATEGORY;
import static com.duykhanh.storeapp.utils.Constants.KEY_ITEM_VIEW;
import static com.duykhanh.storeapp.utils.Constants.KEY_READ_ALL_COMMENT;
import static com.duykhanh.storeapp.utils.Constants.KEY_RELEASE_TO;
import static com.duykhanh.storeapp.utils.Constants.KEY_TEM_POSITION_SALE;


public class ProductDetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, ProductDetailContract.View, View.OnClickListener {
    final String TAG = this.getClass().toString();
    boolean isHideToolbarView = false;
    int dotsCount;
    int sumQuanity;
    String productId;
    int productQuantity;
    double productPromorionPrice;
    int dataStartActivity;
    boolean isBuyNow;

    ProductDetailPresenter productDetailPresenter;
    List<Comment> comments;
    List<User> uList;
    Product mProduct;
    CartItem cartItem;

    LinearLayoutManager mLayoutManager;
    CommentsAdapter commentsAdapter;

    List<Fragment> fragmentList;
    ImageView[] dots;
    RecyclerView rvComment;
    ViewPager vpProductImgSlide;

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    LinearLayout llDots, llctnComments, layoutColor1, layoutColor2;
    ProgressBar pbProductDetail;
    TextView tvProductName, tvProductPrice, tvProductPricea, tvInStock,
            tvProductId, tvProductMaterial, tvProductSize, tvProductWaranty,
            tvProductDescription, tvProductRating, txt_view_comment_all,
            tvCartCounted,
            tvSeeMore;
    ImageButton ibtnBack, ibtnToCart, ibtnAddToCart;
    RatingBar rbProductRating;
    Button btnToComment, btnBuy;


    Formater formater;

    ImageButton btnShoppingAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        uList = new ArrayList<>();
        //Ánh xạ UI
        initUI();
        //Khởi tạo thành phần
        initComponent();
        //Thiết lập chung cho RecyclerView hiển thị Comment (chưa đưa dữ liệu vào)
        settingCommentsRecyclerView();
        //Lấy Id của Product

        Intent intent = getIntent();

        if (intent.getSerializableExtra(KEY_RELEASE_TO) != null) {
            productId = intent.getStringExtra(KEY_RELEASE_TO);
        }

        if (intent.getSerializableExtra(KEY_ITEM_CATEGORY) != null) {
            productId = intent.getStringExtra(KEY_ITEM_CATEGORY);
        }

        Log.d(TAG, "onCreate: productId" + productId);
        if (intent.getStringExtra(KEY_ITEM_VIEW) != null) {
            productId = intent.getStringExtra(KEY_ITEM_VIEW);
        }

        if (intent.getIntExtra("KEY_START_HOMESCREEN", 0) != 0) {
            dataStartActivity = intent.getIntExtra("KEY_START_HOMESCREEN", 0);
        }

        if (intent.getIntExtra("KEY_START_CATEGORY", 0) != 0) {
            dataStartActivity = intent.getIntExtra("KEY_START_CATEGORY", 0);
        }

        if(intent.getStringExtra(KEY_TEM_POSITION_SALE) != null){
            productId = intent.getStringExtra(KEY_TEM_POSITION_SALE);
        }

        if (productId != null) {
            productDetailPresenter.requestIncreaseView(productId);
        }

        //Sự kiệu onclick các kiểu
        ibtnBack.setOnClickListener(this);
        ibtnAddToCart.setOnClickListener(this);
        ibtnToCart.setOnClickListener(this);
        btnToComment.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        txt_view_comment_all.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);

        tvSeeMore.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        cleanData();
        productDetailPresenter.requestProductFromServer(productId);
        productDetailPresenter.requestCommentsFromServer(productId);
        productDetailPresenter.requestCartCounter();
    }

    private void cleanData() {//Làm mới lại dữ liệu
        comments.clear();
        sumQuanity = 0;
        mProduct = null;
        dots = null;
        dotsCount = 0;
        isBuyNow = false;
    }

    @SuppressLint("SetTextI18n")
    @Override//Hiển thị số lượng hàng trong giỏ hàng
    public void setCartItemCounter(int productQuantity) {
        sumQuanity = productQuantity;
        tvCartCounted.setVisibility(View.VISIBLE);
        tvCartCounted.setText(sumQuanity + "");
        if (sumQuanity == 0) {
            tvCartCounted.setVisibility(View.GONE);
        }
        //Chuyển sang màn hình giỏ hàng
        if (isBuyNow){
            startActivity(new Intent(this,OrderActivity.class));
        }
    }

    @Override//Đưa dữ liệu vào view
    public void setDataToView(Product product) {
        mProduct = product;
        bindDataToSlide(mProduct.getImg());
        bindDataToDetail(mProduct);
    }

    @Override
    public void setCommentsToRecyclerView(List<Comment> commentss) {
        comments.clear();
        if (commentss.size() == 0) {
            txt_view_comment_all.setVisibility(View.GONE);
            return;
        }
        for (int i = 0; i < 3; i++) {
            comments.add(commentss.get(i));
            if (i == commentss.size() - 1) {
                break;
            }
        }
        txt_view_comment_all.setVisibility(View.VISIBLE);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtnBack:
                super.onBackPressed();
                break;
            case R.id.imgbtnShoppingAdd:
                addProductToCart();
                break;
            case R.id.btnBuy:
                isBuyNow = true;
                addProductToCart();
                break;
            case R.id.imgbtnShopping:
                startActivity(new Intent(ProductDetailActivity.this, OrderActivity.class));
                if (KEY_DATA_HOME_TO_DETAIL_PRODUCT == dataStartActivity) {
                    Intent iHome = new Intent(ProductDetailActivity.this, MainActivity.class);
                    setResult(RESULT_OK, iHome);
                    finish();
                    return;
                }

                if (KEY_DATA_CATEGORY_TO_DETAIL_PRODUCT == dataStartActivity) {
                    Intent iCategory = new Intent(ProductDetailActivity.this, CategoryListProductActivity.class);
                    setResult(RESULT_OK, iCategory);
                    finish();
                    return;
                }
                break;
            case R.id.btnToComment:
                Intent iComment = new Intent(ProductDetailActivity.this, CommentProductActivity.class);
                iComment.putExtra(KEY_COMMENT_PRODUCT, mProduct);
                startActivity(iComment);
                break;
            case R.id.txt_view_comment_all:
                Intent iCommentMore = new Intent(ProductDetailActivity.this, MoreCommentProductActivity.class);
                iCommentMore.putExtra(KEY_READ_ALL_COMMENT,productId);
                startActivity(iCommentMore);
                break;
            case R.id.tvSeeMore:
                tvProductDescription.setMaxLines(Integer.MAX_VALUE);
                tvSeeMore.setVisibility(View.GONE);

                break;
        }
    }

    private void addProductToCart() {
        if (!(productQuantity > 0)) {
            Toast.makeText(getApplicationContext(), "Sản phẩm hiện hết hàng\nVui lòng trở lại sau", Toast.LENGTH_SHORT).show();
            return;
        }
        Glide.with(this)
                .asBitmap()
                .load(formater.formatImageLink(mProduct.getImg().get(0)))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                        byte[] imgCart = byteArrayOutputStream.toByteArray();
                        cartItem = new CartItem(mProduct.getId(), mProduct.getNameproduct(), (long) productPromorionPrice,
                                mProduct.getQuantity(), mProduct.getQuantity(), imgCart);
                        productDetailPresenter.addCartItem(cartItem);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void bindDataToDetail(Product product) {
        productPromorionPrice = (product.getPrice()) - (product.getPrice() * product.getPromotion());
        tvProductName.setText(product.getNameproduct());

        BigDecimal bd = new BigDecimal(product.getPoint());
        Log.d(TAG, "bindDataToDetail: " + product.getPoint());
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        float rounded = bd.floatValue();
        tvProductRating.setText(rounded + "/5");
        rbProductRating.setRating(rounded);

        tvProductId.setText(product.getId());
        tvProductMaterial.setText(product.getMaterial());
        tvProductSize.setText(product.getSize());
        tvProductWaranty.setText(product.getWarranty());
        tvProductDescription.setText(product.getDescription());
        if (tvProductDescription.getLineCount() < 10) {
            tvSeeMore.setVisibility(View.GONE);
        } else {
            tvSeeMore.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "bindDataToDetail: " + product.getPromotion());
        //Khuyến mãi
        tvProductPrice.setText(Formater.formatMoney((int) productPromorionPrice) + " vnđ");
        if (product.getPromotion() != 0) {
            tvProductPricea.setText(Formater.formatMoney(product.getPrice()) + " vnđ");
            tvProductPricea.setPaintFlags(tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (product.getQuantity() > 0) {
            productQuantity = product.getQuantity();
            tvInStock.setText("Còn hàng");
            ibtnAddToCart.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
            btnBuy.setText("Mua Hàng");
        } else {
            tvInStock.setText("Hết hàng");
            ibtnAddToCart.setImageResource(R.drawable.ic_remove_shopping_cart_black_24dp);
            btnBuy.setText("Hết Hàng");
        }
    }

    private void settingCommentsRecyclerView() {
        mLayoutManager = new LinearLayoutManager(ProductDetailActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvComment.setLayoutManager(mLayoutManager);
        rvComment.setItemAnimator(new DefaultItemAnimator());
        rvComment.setAdapter(commentsAdapter);
    }

    @Override
    public void onCommentsResponseFailure(Throwable throwable) {
        Log.e(TAG, "onCommentsResponseFailure: ", throwable);
        Toast.makeText(this, "Lỗi khi hiển thị Bình Luận", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartItemCountResponseFailure(Throwable throwable) {
        Log.e(TAG, "onCartItemCountResponseFailure: ", throwable);
        Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendInfomationUser(List<User> userList) {
        uList.addAll(userList);
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFaild() {

    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.e(TAG, "onResponseFailure: ", throwable);
        Toast.makeText(this, "Có lỗi khi tải dữ liệu\nVui lòng thử lại sau...", Toast.LENGTH_SHORT).show();
    }


    private void initComponent() {
        productDetailPresenter = new ProductDetailPresenter(this);
        productDetailPresenter.requestInfomationUser();
        comments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(ProductDetailActivity.this, comments, R.layout.item_comments);
        formater = new Formater();
    }

    private void initUI() {
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        layoutColor1 = findViewById(R.id.layoutColor1);
        layoutColor2 = findViewById(R.id.layoutColor2);
        llDots = findViewById(R.id.layoutDots);
        vpProductImgSlide = findViewById(R.id.viewpagerSlider);
        pbProductDetail = findViewById(R.id.pbProductDetail);
        rbProductRating = findViewById(R.id.ratingbarPointProductDetail);
        rvComment = findViewById(R.id.rvComments);

        llctnComments = findViewById(R.id.llctnComments);
        txt_view_comment_all = findViewById(R.id.txt_view_comment_all);
        btnToComment = findViewById(R.id.btnToComment);
        btnBuy = findViewById(R.id.btnBuy);
        ibtnBack = findViewById(R.id.imgbtnBack);
        ibtnAddToCart = findViewById(R.id.imgbtnShoppingAdd);
        ibtnToCart = findViewById(R.id.imgbtnShopping);
        tvProductName = findViewById(R.id.txtNameProductDetail);
        tvProductPrice = findViewById(R.id.txtPriceProductDetail);
        tvProductPricea = findViewById(R.id.txtPriceProductDetaila);
        tvProductId = findViewById(R.id.txtIdProductDetail);
        tvProductMaterial = findViewById(R.id.txtMaterialProductDetail);
        tvProductSize = findViewById(R.id.txtSizeProductDetail);
        tvProductWaranty = findViewById(R.id.txtWarrantyProductDetail);
        tvProductDescription = findViewById(R.id.txtDesProductDetail);
        tvProductRating = findViewById(R.id.txtPointProductDetail);
        tvCartCounted = findViewById(R.id.txtSizeShopping);
        tvInStock = findViewById(R.id.tvInStock);
        tvSeeMore = findViewById(R.id.tvSeeMore);
    }

    @Override
    public void showProgress() {
        pbProductDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbProductDetail.setVisibility(View.GONE);

    }

    //Đưa hình ảnh vào slide
    private void bindDataToSlide(List<String> linkImg) {
//        Thiết lập hình
        fragmentList = new ArrayList<>();
        for (int i = 0; i < linkImg.size(); i++) {
            ProductDetailSlideFragment productDetailSlideFragment = new ProductDetailSlideFragment();
            Bundle bundle = new Bundle();
            bundle.putString("link", linkImg.get(i));
            productDetailSlideFragment.setArguments(bundle);
            fragmentList.add(productDetailSlideFragment);
        }
        SlideAdapter adapter = new SlideAdapter(getSupportFragmentManager(), fragmentList);
        vpProductImgSlide.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        Set chấm tròn dưới slide
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];
        llDots.removeAllViews();
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_circle_grey));
//            set kích thước của chấm tròn
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,
                    20);
            params.setMargins(8, 0, 8, 0);
            llDots.addView(dots[i], params);
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_circle_white));
        }

        vpProductImgSlide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_circle_grey));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_circle_white));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;
        if (percentage == 1f && isHideToolbarView) {
            Log.d(TAG, "onOffsetChanged: 1");
            layoutColor1.setBackgroundResource(0);
            layoutColor2.setBackgroundResource(0);
            isHideToolbarView = !isHideToolbarView;
        } else if (percentage < 1f && !isHideToolbarView) {
            Log.d(TAG, "onOffsetChanged: 2");
            layoutColor1.setBackgroundResource(R.drawable.circle_menu_toolbar);
            layoutColor2.setBackgroundResource(R.drawable.circle_menu_toolbar);
            isHideToolbarView = !isHideToolbarView;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        productDetailPresenter.onDestroy();
    }
}

