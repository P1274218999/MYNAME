package com.dhht.sld.base;


import com.dhht.annotation.MvpEmptyViewFactory;
import com.dhht.mvp.IMvpView;
import com.dhht.mvp.base.BaseMvpPresenter;
import com.dhht.task.LfTask;
import com.dhht.task.TaskHelper;

/**
 * Created by anson on 2019/1/13. 集成mvp 及 网络请求 快捷方式
 */
public class BasePresenter<T extends IMvpView> extends BaseMvpPresenter<T> {

    public BasePresenter(T view) {
        super(view);
    }

    protected void submitTask(LfTask task) {
        TaskHelper.submitTask(task,task);
    }

    @Override
    protected T getEmptyView() {
        T t = null;
        Class superClassGenricType = GenericsUtils.getSuperClassGenricType(this.getClass(), 0);
        try {
//            t = (T) MvpEmptyViewFactory.create(superClassGenricType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
