package com.dhht.sld.main.choose.pay.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dhht.sld.R;
import com.dhht.sld.base.BaseActivity;
import com.dhht.sld.base.http.retorfit.Converter;
import com.dhht.sld.base.http.retorfit.Result;
import com.dhht.sld.base.http.retorfit.Retorfit;
import com.dhht.sld.base.inject.ViewInject;
import com.dhht.sld.main.choose.pay.bean.PayOrderBean;
import com.dhht.sld.main.choose.pay.bean.PriceBean;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tamsiree.rxpay.wechat.pay.WechatPayTools;
import com.tamsiree.rxkit.interfaces.OnSuccessAndErrorListener;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间:2020/8/13  10:06
 * 文件描述: 支付方式界面
 */

@ViewInject(mainLayoutId = R.layout.activity_pay)
public class PayActivity extends BaseActivity   {
    @BindView(R.id.pay_checkBox_wechat)
    TextView checkBoxWeChat;
    @BindView(R.id.pay_checkBox_zhifubao)
    TextView checkBoxZhiFuBao;
    @BindView(R.id.pay_price)
    TextView payPrice;
    @BindView(R.id.pay_btn)
    QMUIRoundButton payBtn;
    private Integer payMethod = -1;
    int status;
    int find_order_id;

    @Override
    public void afterBindView() {
        Bundle extras = getIntent().getExtras();
        find_order_id = extras.getInt("find_order_id");
        status = extras.getInt("status");
        payBtn.setBackgroundColor(getResources().getColor(R.color.gray));
        payBtn.setChangeAlphaWhenPress(true);
        Retorfit.getService().getOrderStatus(find_order_id+"").enqueue(new Converter<Result<PriceBean>>() {
            @Override
            public void onSuccess(Result<PriceBean> priceBeanResult) {
                DecimalFormat format=new DecimalFormat(".00元");
                payPrice.setText(format.format(priceBeanResult.data.price));
            }
        });
    }


    @OnClick({R.id.pay_wechat, R.id.pay_zhifubao, R.id.pay_btn,R.id.toolbar_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_zhifubao:
                checkBoxZhiFuBao.setTextColor(getResources().getColor(R.color.appTheme));
                checkBoxWeChat.setTextColor(getResources().getColor(R.color.gray));
                payBtn.setBackgroundColor(getResources().getColor(R.color.appTheme));
                payMethod = 2;
                break;
            case R.id.pay_wechat:
                checkBoxZhiFuBao.setTextColor(getResources().getColor(R.color.gray));
                checkBoxWeChat.setTextColor(getResources().getColor(R.color.appTheme));
                payBtn.setBackgroundColor(getResources().getColor(R.color.appTheme));
                payMethod = 1;
                break;
            case R.id.pay_btn:
                if (payMethod == -1) break;
                Retorfit.getService().createOrder(
                        payMethod+"",
                        find_order_id+"").enqueue(new Converter<Result<PayOrderBean>>() {
                    @Override
                    public void onSuccess(Result<PayOrderBean> payOrderBeanResult) {
                        resPay(payOrderBeanResult.data);
                    }
                });
                break;
            case R.id.toolbar_back:
                finish();
                break;
        }
    }

    /**发起支付
     * @param
     */
    public void resPay(PayOrderBean res) {
        if (res.code >= 0)
            if (payMethod == 1) {
//            WechatPayModel wechatPayModel=new WechatPayModel(
//                    res.data.appid,
//                    res.data.partnerid,
//                    "Sign=WechatPay",
//                    res.data.prepayid,
//                    res.data.noncestr,
//                    res.data.timestamp,
//                    res.data.sign);
//            String pay_param = new Gson().toJson(wechatPayModel);
//            WechatPayTools.doWXPay(mActivity,res.data.appid, pay_param, new OnSuccessAndErrorListener() {
//                @Override
//                public void onSuccess(String s) {
//                    RxToast.error(s);
//                }
//
//                @Override
//                public void onError(String s) {
//                    RxToast.error(s);
//                }
//            });
                WechatPayTools.wechatPayApp(this,
                        res.appid, //微信分配的APP_ID
                        res.partnerid, //微信分配的 PARTNER_ID (商户ID)
                        res.sign, //微信分配的 PRIVATE_KEY (私钥)
                        res.prepayid, //订单ID (唯一)
                        new OnSuccessAndErrorListener() {
                            @Override
                            public void onSuccess(String s) {
                            }

                            @Override
                            public void onError(String s) {
                            }
                        });

            }
    }
}
