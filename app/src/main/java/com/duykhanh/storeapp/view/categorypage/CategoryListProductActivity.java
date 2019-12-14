package com.duykhanh.storeapp.view.categorypage;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.category.ProductAdapterCategory;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.presenter.category.CategoryProductListContract;
import com.duykhanh.storeapp.presenter.category.CategoryProductListPresenter;
import com.duykhanh.storeapp.view.MainActivity;
import com.duykhanh.storeapp.view.order.OrderActivity;
import com.ligl.android.widget.iosdialog.IOSSheetDialog;

import java.util.ArrayList;
import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_CATEGORY;
import static com.duykhanh.storeapp.utils.Constants.KEY_DATA_CATEGORY_PRODUCT_CART;
import static com.duykhanh.storeapp.utils.Constants.KEY_DATA_CATEGORY_PRODUCT_SEARCH;
import static com.duykhanh.storeapp.utils.Constants.KEY_TITLE;

/**
 * Created by Duy Khánh on 11/26/2019.
 */
public class CategoryListProductActivity extends AppCompatActivity implements CategoryProductListContract.View, View.OnClickListener, DialogInterface.OnClickListener {

    private static final String TAG = CategoryListProductActivity.class.getSimpleName();
    CategoryProductListContract.Presenter mPresenter;

    View icl_product_list_category;
    View icl_slide_show_cateogry;
    View icl_toolbar_category;

    RecyclerView rcl_product_list;
    TextView txt_size_shop;
    Button btn_filter;
    ImageButton imgBackCategory, btn_category_to_cart;
    ProgressBar pb_load;

    TextView edFind;

    List<Product> listProduct;
    ProductAdapterCategory adapterProduct;

    GridLayoutManager mLayoutManager;

    String id_category, title_category;
    int loadmore = 999;

    int pageNo = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private int previousTotal = 0; // Tổng số item khi yêu cầu dữ liệu trên server
    private boolean loading = true; // Trạng thái load dữ liệu
    private int visibleThreshold = 4;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list_product);

        getData();

        initUI();
        initSlideCommon();
        initRecyclerView();
        registerListener();
        onListenerScrollView();
    }

    private void initUI() {
        icl_product_list_category = findViewById(R.id.icl_product_list_category);
        icl_slide_show_cateogry = findViewById(R.id.icl_slide_show_cateogry);
        icl_toolbar_category = findViewById(R.id.icl_toolbar_category);

        edFind = icl_toolbar_category.findViewById(R.id.edtFind);
        txt_size_shop = icl_toolbar_category.findViewById(R.id.txtSizeShoppingHome);

        rcl_product_list = icl_product_list_category.findViewById(R.id.rcl_CategoryProductList);
        imgBackCategory = icl_toolbar_category.findViewById(R.id.img_back_category);
        btn_category_to_cart = icl_toolbar_category.findViewById(R.id.imgbtnSizeShop);
        btn_filter = icl_product_list_category.findViewById(R.id.btn_filter_price);
        pb_load = icl_product_list_category.findViewById(R.id.pb_load_category);

        edFind.setText(title_category);
    }

    // Khởi tạo các object cần thiết

    private void initSlideCommon() {
        pageNo = 1;

        mPresenter = new CategoryProductListPresenter(this, this);

        //Gửi yêu vầu và key category lên server
        mPresenter.requestDataFromServer(id_category, pageNo);
        mPresenter.requestDataCountFormDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.requestDataCountFormDB();
    }

    // Khởi tạo recyclerview

    private void initRecyclerView() {
        listProduct = new ArrayList<>();
        mLayoutManager = new GridLayoutManager(this, 2);
        rcl_product_list.setLayoutManager(mLayoutManager);
        rcl_product_list.setItemAnimator(new DefaultItemAnimator());
        adapterProduct = new ProductAdapterCategory(this, listProduct);
        rcl_product_list.setAdapter(adapterProduct);
    }

    // Lấy key category truyền từ CategoryFragment
    private void getData() {
        Intent intent = getIntent();
        id_category = intent.getStringExtra(KEY_CATEGORY);
        title_category = intent.getStringExtra(KEY_TITLE);
    }

    private void registerListener() {
        edFind.setOnClickListener(this);
        imgBackCategory.setOnClickListener(this);
        btn_category_to_cart.setOnClickListener(this);
        btn_filter.setOnClickListener(this);
    }

    private void onListenerScrollView() {
        rcl_product_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();// Số lượng item đang hiển thị trên màn hình
                totalItemCount = mLayoutManager.getItemCount();// Tổng item đang có trên view
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();// Vị trí item hiển thị đầu tiên kho scroll view
                if (loading) {
                    // Nếu tổng item lớn hơn tổng số item trước đó thì gán nó cho biến previousTotal
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    if (loadmore == 999) {
                        mPresenter.getMoreListCategory(id_category, pageNo);
                    } else if (loadmore == 998) {
                        mPresenter.getMoreListHightPriceProduct(id_category, pageNo);
                    } else if (loadmore == 997) {
                        mPresenter.getMoreListLowPriceProduct(id_category, pageNo);
                    }
                    loading = true;
                }
            }
        });
    }

    // // Nhận list product được gửi từ presenter
    @Override
    public void senDataToRecyclerView(List<Product> productList) {
        loadmore = 999;
        listProduct.addAll(productList);
        adapterProduct.notifyDataSetChanged();

        pageNo++;
    }

    @Override
    public void sendDataToRecyclerViewHigh(List<Product> productHight) {
        loadmore = 998;
        listProduct.clear();
        listProduct.addAll(productHight);
        adapterProduct.notifyDataSetChanged();

        pageNo++;
    }

    @Override
    public void sendDataToRecyclerViewLow(List<Product> productView) {
        loadmore = 997;
        listProduct.clear();
        listProduct.addAll(productView);
        adapterProduct.notifyDataSetChanged();

        pageNo++;
    }

    @Override
    public void sendCountProduct(int countProduct) {
        txt_size_shop.setText("" + countProduct);
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.d("ERROR", "onResponseFailure: " + throwable);
    }


    @Override
    public void hiddenProgressBar() {
        pb_load.setVisibility(View.GONE);
    }

    @Override
    public void showSizeCart() {
        txt_size_shop.setVisibility(View.VISIBLE);
    }

    @Override
    public void hiddenSizeCart() {
        txt_size_shop.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_category:
                finish();
                break;
            case R.id.imgbtnSizeShop:
                Intent iViewProductCart = new Intent(CategoryListProductActivity.this, OrderActivity.class);
                startActivity(iViewProductCart);
                break;
            case R.id.btn_filter_price:
                dialogSortBuy();
                break;
        }
    }

    private void dialogSortBuy() {
        IOSSheetDialog.SheetItem[] items = new IOSSheetDialog.SheetItem[2];
        items[0] = new IOSSheetDialog.SheetItem("Giá cao nhất", IOSSheetDialog.SheetItem.BLUE);
        items[1] = new IOSSheetDialog.SheetItem("Giá thấp nhất", IOSSheetDialog.SheetItem.BLUE);
        new IOSSheetDialog.Builder(this)
                .setData(items, this::onClick).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent iViewProduct = new Intent(CategoryListProductActivity.this, MainActivity.class);
            iViewProduct.putExtra("KEY_START_CATEGORY_PRODUCT_CART", KEY_DATA_CATEGORY_PRODUCT_CART);
            setResult(RESULT_OK, iViewProduct);
            finish();
        }
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == 0) {
            pageNo = 1;
            mPresenter.requestDataHighPriceFromServer(id_category, pageNo);
        } else {
            pageNo = 1;
            mPresenter.requestDataLowPriceFromServer(id_category, pageNo);
        }
    }
}
