package com.dhht.sld.base.http.retorfit;

import com.dhht.sld.main.choose.pay.bean.PayOrderBean;
import com.dhht.sld.main.choose.pay.bean.PriceBean;
import com.dhht.sld.main.findorder.bean.FindOrderBean;
import com.dhht.sld.main.notification.bean.MessageBean;
import com.dhht.sld.main.wallet.bean.UserMoneyBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 获取订单信息
     *
     * @param id
     * @return
     */
    @POST("/findOrder/getOrderStatus")
    Call<Result<PriceBean>> getOrderStatus(@Query("id") String id);


    /**
     * 发起支付
     *
     * @param from_type
     * @param order_id
     * @return
     */
    @POST("/pay/createOrder")
    Call<Result<PayOrderBean>> createOrder(@Query("from_type") String from_type,
                                           @Query("order_id") String order_id);

    /**
     * 获取用户余额
     */
    @POST("/user/getAssets")
    Call<Result<UserMoneyBean>> getAssets();


    @GET("/messages/getList")
    Call<Result<List<MessageBean>>> getMessageList(
            @Query("page") Integer page,
            @Query("page_size") Integer page_size,
            @Query("type") Integer type);

    /**
     * @param id
     * @return
     */
    @GET("/messages/updatestatus")
    Call<Result<Integer>> updateStatus(@Query("msg_id")Integer id);

    /**获取未读消息总条数
     * @param status 1已读信息 2未读信息
     * @return
     */
    @GET("/messages/checkstatus")
    Call<Result<Integer>> checkStatus(@Query("status")Integer status);


    @GET("/findOrder/getlist")
    Call<Result<List<FindOrderBean>>> getOrderList(@Query("page")Integer page,
                                             @Query("page_size")Integer page_size);
}
