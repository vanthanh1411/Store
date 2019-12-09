package com.duykhanh.storeapp.network;

import com.duykhanh.storeapp.model.Category;
import com.duykhanh.storeapp.model.Comment;
import com.duykhanh.storeapp.model.Order;
import com.duykhanh.storeapp.model.OrderDetail;
import com.duykhanh.storeapp.model.Product;
import com.duykhanh.storeapp.model.ProductResponse;
import com.duykhanh.storeapp.model.ResponseComment;
import com.duykhanh.storeapp.model.SlideHome;
import com.duykhanh.storeapp.model.SlideSale;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Duy Khánh on 11/5/2019.
 */
public interface DataClient {
//    http://strdecor.herokuapp.com/api/str1/slider
//    @GET("slider")
//    Call<List<>>

    // Yêu cầu tất cả dữ liệu sản phẩm
    @GET("product")
    Call<List<Product>> getDataProduct(@Query("page") int PageNo);

    // Yêu cầu dữ liệu sản phảm theo id
    @GET("product/{idProduct}")
    Call<Product> getProductDetail(@Path("idProduct") String productId);

    @GET("product/idcategory/{idcategory}")
    Call<List<Product>> getProductListCategory(@Path("idcategory") String id_category, @Query("page") int PageCategory);

    @GET("product/idcategory/{idcategory}/sort-high-price")
    Call<List<Product>> getProductSortHighPrice(@Path("idcategory") String id_category, @Query("page") int pageHigh);

    @GET("product/idcategory/{idcategory}/sort-low-price")
    Call<List<Product>> getProductSortLowPrice(@Path("idcategory") String id_category, @Query("page") int pageLow);

    //Yêu cầu dữ liệu sản phẩm theo view
    @GET("product/sort/by-view")
    Call<List<Product>> getProductView(@Query("page") int PageView);

    @GET("product/sort/by-buy")
    Call<List<ProductResponse>> getProductBuy(@Query("page") int PageBuy);

    @GET("utiliti")
    Call<List<SlideHome>> getUtiliti();

    @GET("utiliti/{idp}")
    Call<List<SlideSale>> getProductSale(@Path("idp") String productId);

    @GET("comment/idp/{idp}")
    Call<List<Comment>> getCommentByIdp(@Path("idp") String productId);

    @Multipart
    @POST("comment")
    Call<ResponseComment> postComment(@Part List<MultipartBody.Part> parts, @PartMap Map<String,RequestBody> map);

    @GET("category")
    Call<List<Category>> getCategory();

    @POST("order")
    Call<Order> postOrder(@Body Order order);

    @POST("orderdetail")
    Call<OrderDetail> postOrderDetail(@Body OrderDetail orderDetail);

    @GET("product")
    Call<List<Product>> getProductByKey(@Query("name") String searchKey, @Query("page") int pageNo);

    @PUT("product/{idproduct}")
    Call<Product> putViewProductUp(@Path("idproduct") String productId);

    @GET("order/idu/{iduser}/statusId/{statusId}")
    Call<List<Order>> getOrders(@Path("iduser") String userId, @Path("statusId") int orderStatus);

    @GET("orderdetail/ido/{idorder}") //GET list Order Detail
    Call<List<OrderDetail>> getOrderDetails(@Path("idorder") String orderId);


}
