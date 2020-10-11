package com.dhht.sld.main.updateapp.contract;

import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.ILifeCircle;
import com.dhht.mvp.IMvpView;
import com.dhht.sld.main.address.bean.AddressResBean;
import com.dhht.sld.main.updateapp.bean.CheckVersionBean;

/**
 * 作者：pst
 * 邮箱：1274218999@qq.com
 * 创建时间：2020/7/22  17:01
 * 文件描述：app检测更新抽象层
 */
public interface CheckVersionContract {

    @MvpEmptyViewFactory
    interface Iview extends IMvpView {
        void  resDoSubmit(CheckVersionBean data);
    }

    interface IPresenter extends ILifeCircle {
        void doSubmit();
    }
}
