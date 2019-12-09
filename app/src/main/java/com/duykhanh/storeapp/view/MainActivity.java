package com.duykhanh.storeapp.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.duykhanh.storeapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
* Điều khiển hoạt động chính của ứng dụng với bố cục 'R.layout.activity_main'
* chứa Navigation (@link https://developer.android.com/guide/navigation) hiển thị danh sách thông tin về :
*   + Trang chủ
*   + Danh mục
*   + Cá nhân
*   + Thông báo
*
*   @author Lê Duy Khánh
*/
import static com.duykhanh.storeapp.utils.Constants.*;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    int key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        initUI();
         /*
         * Mỗi id button trong menu tương ứng với id menu khai báo trong navigation
         */
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
    // Ánh xạ giao diện
    private void initUI() {
        bottomNavigationView = findViewById(R.id.nav_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK) {
                int key_cart = data.getIntExtra("KEY_START_CATEGORY_PRODUCT_CART",0);
                int key_find = data.getIntExtra("KEY_START_CATEGORY_PRODUCT_SEARCH",0);

                if(key_cart == KEY_DATA_CATEGORY_PRODUCT_CART){
                    Fragment nCart = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController c = NavHostFragment.findNavController(nCart);
                    c.navigate(R.id.navCart);
                }
                if(key_find == KEY_DATA_CATEGORY_PRODUCT_SEARCH){
                    Fragment nSearch = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController cSearch = NavHostFragment.findNavController(nSearch);
                    cSearch.navigate(R.id.navSearch);
                }
            }

    }
}
