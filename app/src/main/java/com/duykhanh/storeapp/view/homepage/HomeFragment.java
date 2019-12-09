package com.duykhanh.storeapp.view.homepage;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.buyviewproduct.BuyProductAdapter;
import com.duykhanh.storeapp.adapter.homes.ProductAdapter;
import com.duykhanh.storeapp.adapter.homes.SlideshowAdapter;
import com.duykhanh.storeapp.adapter.viewproduct.ViewProductAdapter;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.SlideHome;
import com.duykhanh.storeapp.presenter.home.HomePresenter;
import com.duykhanh.storeapp.presenter.home.ProductListContract;
import com.duykhanh.storeapp.view.homepage.buythemostpage.BuyMostActivity;
import com.duykhanh.storeapp.view.homepage.viewproductpage.ViewProductActivity;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_DATA_HOME_TO_DETAIL_PRODUCT;
import static com.duykhanh.storeapp.utils.Constants.KEY_RELEASE_TO;
import static com.duykhanh.storeapp.utils.Constants.KEY_START_BUY_PRODUCT;
import static com.duykhanh.storeapp.utils.Constants.KEY_START_DETAIL_PRODUCT;
import static com.duykhanh.storeapp.utils.Constants.KEY_START_VIEW_PRODUCT;

/**
 * Created by Duy Khánh on 11/6/2019.
 * <p>
 * View Trang chủ chứa layout hiển thị danh sách Product đượcw lấy từ model
 * bằng cách implement interface {@link ProductListContract.View}
 * thông qua {@link HomePresenter} lớp Presenter bởi .
 */
@SuppressWarnings("ALL")
public class HomeFragment extends Fragment implements ProductListContract.View,
        ProductItemClickListener, View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();

    // Presenter interface dùng cho view
    private static ProductListContract.Presenter mPresenter;
    // Khởi tạo view
    View view;

    /*
     * Các view hiển thị trong màn hình home
     */
    ProgressBar progressBarLoadProduct; // Load khi lấy dữ liệu product
    NestedScrollView nestedScrollViewHome; // Chứa toàn bộ view lấy out trừ toolbar
    SwipeRefreshLayout swipeRefreshLayoutHome;// Refresh lại layout khi kéo màn hình xuống
    RecyclerView recyclerViewProduct, rcl_view_product, rcl_buy_product; // Hiển thị danh sách các sản phẩm

    // Slide show hiển thị banner thông báo
    SliderView sliderView;

    /*
     * Xem thêm phần lượt xem nhiều nhất
     * Xem thêm phần lượt mua nhiều nhất
     */
    TextView txt_view_all, txt_buy_all;

    /*
     * Các view năm trên thanh toolbar bao gồm
     * Nút tìm kiếm
     * Nút di chuyển sang màn hình giỏ hàng
     * Text hiển thị số lượng sản phẩm trong giỏ hàng
     */
    TextView edFind;
    ImageButton btnCartShop;
    TextView txtSizeShoppingHome;

    /*
     * Khởi tạo các adapter cần thiết để xây dựng giao diện
     */
    SlideshowAdapter slideshowAdapter;
    ProductAdapter productAdapter;
    ViewProductAdapter viewProductAdapter;
    BuyProductAdapter buyProductAdapter;

    /*
     * Kiểu danh sách hiển thị trên recyclerview
     */
    GridLayoutManager mLayoutManager;
    LinearLayoutManager viewLinearLayoutManager;
    LinearLayoutManager buyLinearLayoutManager;

    //Slideshow
    AdapterViewFlipper avfSlideshow;
    /*
     * Danh sách sản phẩm:
     * + Lượt xem nhiều nhất
     * + Luợt mua nhiều nhất
     * + Tất cả
     */
    private List<Product> viewProductList;
    private List<Product> buyProductList;
    private List<Product> productList;
    private List<SlideHome> slideHomes;
    /*
     * Phân trang sản phẩm:
     * + Lượt xem nhiều nhất
     * + Lượt mua nhiều nhất
     * + Tất cả
     */
    private int pageView = 0;
    private int pageBuy = 0;
    private int pageNo = 0;

    private int previousTotal = 0; // Tổng số item khi yêu cầu dữ liệu trên server
    private boolean loading = true; // Trạng thái load dữ liệu
    private int visibleThreshold = 4;//

    private int previousTotal_view = 0; // Tổng số item khi yêu cầu dữ liệu trên server
    private boolean loading_view = true; // Trạng thái load dữ liệu
    private int visibleThreshold_view = 4;//

    private int previousTotal_buy = 0; // Tổng số item khi yêu cầu dữ liệu trên server
    private boolean loading_buy = true; // Trạng thái load dữ liệu
    private int visibleThreshold_buy = 4;//

    int firstVisibleItem, visibleItemCount, totalItemCount;
    int firstVisibleItem_view, visibleItemCount_view, totalItemCount_view;
    int firstVisibleItem_buy, visibleItemCount_buy, totalItemCount_buy;

    private int pastVisiblesItems;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // TODO:( in function) Ánh xạ giao diện xml
        initUI();

        // TODO:( in function) Khởi tạo slide show
        initSlideShow();

        //TODO:( in function)  Khởi tạo các thành phân cần thiết
        initializationComponent();

        // TODO:( in function) Lắng nghe các tương tác của người dùng với view
        setListeners();

        Log.d(TAG, "onCreateView: ");

        // TODO:( in function) Đăng ký sự kiện tương tác người dùng với view
        registerListener();

        ViewCompat.setNestedScrollingEnabled(recyclerViewProduct, false);
        return view;
    }

    private void initUI() {
        //TODO:( in function) SlideShow
        sliderView = view.findViewById(R.id.imageSlider);
        //TODO:( in function) Lượt xem
        recyclerViewProduct = view.findViewById(R.id.recyclerProducts);
        rcl_view_product = view.findViewById(R.id.rcl_view_product);
        rcl_buy_product = view.findViewById(R.id.rcl_buy_product);
        txt_view_all = view.findViewById(R.id.txt_view_all);
        txt_buy_all = view.findViewById(R.id.txt_buy_all);

        progressBarLoadProduct = view.findViewById(R.id.progressbarLoadProduct);
        nestedScrollViewHome = view.findViewById(R.id.nestedScrollViewContainerHome);
        swipeRefreshLayoutHome = view.findViewById(R.id.swipeRefreshLayout);
        recyclerViewProduct = view.findViewById(R.id.recyclerProducts);
        edFind = view.findViewById(R.id.edtFind);
        btnCartShop = view.findViewById(R.id.imgbtnSizeShop);
        txtSizeShoppingHome = view.findViewById(R.id.txtSizeShoppingHome);
    }

    // TODO:Khởi tạo các object cần thiết
    private void initSlideShow() {
        slideHomes = new ArrayList<>();

        slideshowAdapter = new SlideshowAdapter(getContext(),slideHomes);
        sliderView.setSliderAdapter(slideshowAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        mPresenter.requestDataCountFormDB();
    }

    private void initializationComponent() {
        mPresenter = new HomePresenter(this, getContext());

        // Cấu hình danh sách lượt xem nhiều nhất
        viewProductList = new ArrayList<>();
        viewLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcl_view_product.setLayoutManager(viewLinearLayoutManager);
        rcl_view_product.setItemAnimator(new DefaultItemAnimator());
        viewProductAdapter = new ViewProductAdapter(this, viewProductList);
        rcl_view_product.setAdapter(viewProductAdapter);

        // Cấu hình danh sách lượt mua nhiều nhất
        buyProductList = new ArrayList<>();
        buyLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcl_buy_product.setLayoutManager(buyLinearLayoutManager);
        rcl_buy_product.setItemAnimator(new DefaultItemAnimator());
        buyProductAdapter = new BuyProductAdapter(this, buyProductList);
        rcl_buy_product.setAdapter(buyProductAdapter);

        // Cấu hình danh sách sản phẩm gợi ý
        ViewCompat.setNestedScrollingEnabled(recyclerViewProduct,false);
        productList = new ArrayList<>();
        mLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewProduct.setAdapter(productAdapter);

        pageView = 1;
        pageNo = 1;
        pageBuy = 1;

        loading = true;
        loading_buy = true;
        loading_view = true;

        // TODO:( in function)  Gửi yếu cầu lên serve
        mPresenter.requestDataFromServer();
        mPresenter.requestDataFromServerView();
        mPresenter.requestDatatFromServerBuy();
        mPresenter.requestDataCountFormDB();
        mPresenter.requestFromDataSlideHome();
    }

    private void registerListener() {
        edFind.setOnClickListener(this);
        txt_view_all.setOnClickListener(this);
        txt_buy_all.setOnClickListener(this);
        btnCartShop.setOnClickListener(this);

        //TODO: Làm mới layout
        swipeRefreshLayoutHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        previousTotal_view = 0;
                        previousTotal_buy = 0;
                        loading_view = true;
                        loading_buy = true;
                        initializationComponent();

                        swipeRefreshLayoutHome.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void setListeners() {
        nestedScrollViewHome.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.d(TAG, "onScrollChange: scrollY");
                    progressBarLoadProduct.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPresenter.getMoreData(pageNo);
                            }
                        }, 1000);
                }
            }
        });

//        //TODO:( in function) Xử lý sự kiện phân trang danh sách lượt xem
        rcl_view_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount_view = recyclerView.getChildCount();// Số lượng item đang hiển thị trên màn hình
                totalItemCount_view = viewLinearLayoutManager.getItemCount();// Tổng item đang có trên view
                firstVisibleItem_view = viewLinearLayoutManager.findFirstVisibleItemPosition();// Vị trí item hiển thị đầu tiên kho scroll view
                if (loading_view) {
                    // Nếu tổng item lớn hơn tổng số item trước đó thì gán nó cho biến previousTotal
                    if (totalItemCount_view > previousTotal_view) {
                        loading_view = false;
                        previousTotal_view = totalItemCount_view;
                    }
                }

                if (!loading_view && (totalItemCount_view - visibleItemCount_view)
                        <= (firstVisibleItem_view + visibleThreshold_view)) {
                    mPresenter.getMoreDataView(pageView);
                    loading_view = true;
                }
            }
        });


        rcl_buy_product.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                visibleItemCount_buy = recyclerView.getChildCount();// Số lượng item đang hiển thị trên màn hình
                totalItemCount_buy = buyLinearLayoutManager.getItemCount();// Tổng item đang có trên view
                firstVisibleItem_buy = buyLinearLayoutManager.findFirstVisibleItemPosition();// Vị trí item hiển thị đầu tiên kho scroll view

                if (loading_buy) {
                    // Nếu tổng item lớn hơn tổng số item trước đó thì gán nó cho biến previousTotal
                    if (totalItemCount_buy > previousTotal_buy) {
                        loading_buy = false;
                        previousTotal_buy = totalItemCount_buy;
                    }
                }

                if (!loading_buy && (totalItemCount_buy - visibleItemCount_buy)
                        <= (firstVisibleItem_buy + visibleThreshold_buy)) {
                    mPresenter.getMoreDataView(pageView);
                    loading_buy = true;
                }
            }
        });
    }

    @Override
    public void showProgress() {
        progressBarLoadProduct.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBarLoadProduct.setVisibility(View.GONE);
    }

    @Override
    public void showSizeCart() {
        txtSizeShoppingHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSizeCart() {
        txtSizeShoppingHome.setVisibility(View.GONE);
    }

    //TODO: Nhận dữ liệu danh sách các sản phẩm được gửi từ presenter
    @Override
    public void sendDataToRecyclerView(List<Product> movieArrayList) {
        productList.addAll(movieArrayList);
        productAdapter.notifyItemInserted(productList.size() - 1);

        pageNo++;
    }

    @Override
    public void sendDataToHorizontalView(List<Product> viewProductArrayList) {
        viewProductList.addAll(viewProductArrayList);
        viewProductAdapter.notifyDataSetChanged();

        pageView++;
    }

    @Override
    public void sendDataToHorizontalBuy(List<Product> buyProductArrayList) {
        buyProductList.addAll(buyProductArrayList);
        buyProductAdapter.notifyDataSetChanged();

        pageBuy++;
    }

    @Override
    public void sendDataToSlideShowHome(List<SlideHome> slideHomeList) {
        slideHomes.addAll(slideHomeList);
        slideshowAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendCountProduct(int countProduct) {
        txtSizeShoppingHome.setText("" + countProduct);
    }

    // TODO: Nhận thông báo lỗi được gửi từ presenter
    @Override
    public void onResponseFailure(Throwable throwable) {
//        Log.e(TAG, throwable.getMessage());
        Log.e(TAG, "onResponseFailure: ", throwable);
    }

    //TODO: Bắt sự kiện click view thực hiện hành động nào đó
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_view_all:
                Intent iViewProduct = new Intent(getContext(), ViewProductActivity.class);
                getActivity().startActivityForResult(iViewProduct, KEY_START_VIEW_PRODUCT);
                break;
            case R.id.txt_buy_all:
                Intent iBuyProduct = new Intent(getContext(), BuyMostActivity.class);
                getActivity().startActivityForResult(iBuyProduct, KEY_START_BUY_PRODUCT);
                break;
            case R.id.imgbtnSizeShop:
                Fragment navCart = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController cCart = NavHostFragment.findNavController(navCart);
                cCart.navigate(R.id.navCart);
                break;
            case R.id.edtFind:
                Fragment navSearch = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController cSearch = NavHostFragment.findNavController(navSearch);
                cSearch.navigate(R.id.navSearch);
                break;
        }
    }

    @Override
    public void onProductItemClick(int position) {
        Intent detailIntent = new Intent(getContext(), ProductDetailActivity.class);
        detailIntent.putExtra(KEY_RELEASE_TO, productList.get(position).getId());
        detailIntent.putExtra("KEY_START_HOMESCREEN", KEY_DATA_HOME_TO_DETAIL_PRODUCT);
        startActivityForResult(detailIntent, KEY_START_DETAIL_PRODUCT);
    }

    //TODO : Click vào mỗi item sản phẩm
    @Override
    public void onProductItemViewclick(int position) {
        Intent detailIntent = new Intent(getContext(), ProductDetailActivity.class);
        detailIntent.putExtra(KEY_RELEASE_TO, viewProductList.get(position).getId());
        detailIntent.putExtra("KEY_START_HOMESCREEN", KEY_DATA_HOME_TO_DETAIL_PRODUCT);
        startActivityForResult(detailIntent, KEY_START_DETAIL_PRODUCT);
        Toast.makeText(getActivity(), "" + viewProductList.get(position).getNameproduct(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

}
