package com.duykhanh.storeapp.view.search;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.adapter.searproduct.ProductSearchedAdapter;
import com.duykhanh.storeapp.adapter.searproduct.SuggestWordsAdapter;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.presenter.search.SearchContract;
import com.duykhanh.storeapp.presenter.search.SearchPresenter;
import com.duykhanh.storeapp.view.productDetails.ProductDetailActivity;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import static com.duykhanh.storeapp.utils.Constants.KEY_RELEASE_TO;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchContract.View, View.OnClickListener, SearchedItemClickListener {
    final String TAG = this.getClass().toString();
    List<Product> products;
    List<String> suggestWords;

    View incSuggestWord;
    View view;
    Toolbar toolbar;
    EditText etSearch;
    RecyclerView rvSearchedProducts, rvSuggestWords;
    ImageView ivSearch;
    ProgressBar pbLoading;

    SearchPresenter presenter;
    ProductSearchedAdapter adapter;
    LinearLayoutManager layoutManager;
    SuggestWordsAdapter suggestWordsAdapter;
    FlexboxLayoutManager flexboxLayoutManager;

    int firstVisibleItem, visibleItemCount, totalItemCount;
    String searchKey = "";
    private int pageNo = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initView();
        initComponent();
        settingToolbar();
        settingRecyclerView();
        presenter.requestSuggestWords();//Yêu cầu lấy danh sách gợi ý
        //Load more
        setRvScrollListener();
        //Phím tìm kiếm thay cho phím Enter
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    cleanProducts();
                    searchKey = v.getText().toString();
                    presenter.requestSearch(searchKey);
                    return true;
                }
                return false;
            }
        });
        ivSearch.setOnClickListener(this);
        //Hiển thị gợi ý khi thay đổi nội dung trong search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                incSuggestWord.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void cleanProducts() {
        pageNo = 1;
        previousTotal = 0;
        loading = true;
        visibleThreshold = 5;
        products.clear();
        adapter.notifyDataSetChanged();
    }

    @Override//Hiển thị danh sách gợi ý
    public void showSuggestWords(List<String> suggestWordss) {
        incSuggestWord.setVisibility(View.VISIBLE);
        suggestWords.clear();
        suggestWords.addAll(suggestWordss);
        suggestWordsAdapter.notifyDataSetChanged();
    }

    @Override//Nhấp vao gợi ý
    public void onSuggestWordClick(int position) {
        cleanProducts();
        searchKey = suggestWords.get(position);
        etSearch.setText(searchKey);
        etSearch.setSelection(searchKey.length());
            presenter.requestSearch(searchKey);
    }

    @Override//Yêu cầu tìm kiếm thành công
    public void requestSearchComplete(List<Product> productss) {
        products.addAll(productss);
        adapter.notifyDataSetChanged();
        pageNo++;
    }

    private void setRvScrollListener() {//Load more
        rvSearchedProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = rvSearchedProducts.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    presenter.requestMoreData(searchKey, pageNo);
                    loading = true;
                }
            }
        });
    }

    @Override
    public void requestSearchFailure(Throwable throwable) {
        Toast.makeText(getContext(), "Có lỗi!", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "requestSearchFailure: ", throwable);
    }

    @Override
    public void hideSuggestWords() {
        incSuggestWord.setVisibility(View.GONE);
    }


    @Override
    public void onSearchedItemClick(int position) {
        Intent detailIntent = new Intent(getContext(), ProductDetailActivity.class);
        detailIntent.putExtra(KEY_RELEASE_TO, products.get(position).getId());
        startActivity(detailIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                cleanProducts();
                searchKey = etSearch.getText().toString();
                presenter.requestSearch(searchKey);
                break;
        }
    }

    private void settingRecyclerView() {
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
        rvSuggestWords.setLayoutManager(flexboxLayoutManager);
        rvSuggestWords.setAdapter(suggestWordsAdapter);

        rvSearchedProducts.setLayoutManager(layoutManager);
        rvSearchedProducts.setAdapter(adapter);
    }

    private void settingToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void initComponent() {
        suggestWords = new ArrayList<>();
        suggestWordsAdapter = new SuggestWordsAdapter(this, R.layout.item_suggestwords, suggestWords);
        flexboxLayoutManager = new FlexboxLayoutManager(getContext());

        products = new ArrayList<>();
        adapter = new ProductSearchedAdapter(SearchFragment.this, products, R.layout.item_product_searched);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        presenter = new SearchPresenter(this);
    }

    private void initView() {
        incSuggestWord = view.findViewById(R.id.incSuggestWords);
        toolbar = view.findViewById(R.id.tbSearching);
        etSearch = view.findViewById(R.id.etSearch);
        rvSearchedProducts = view.findViewById(R.id.rvSearchedProduct);
        ivSearch = view.findViewById(R.id.ivSearch);
        rvSuggestWords = view.findViewById(R.id.rvSuggestWords);
        pbLoading = view.findViewById(R.id.pbLoadingSearch);
    }

    @Override
    public void showProgress() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
