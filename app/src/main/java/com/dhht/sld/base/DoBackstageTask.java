package com.dhht.sld.base;

import com.dhht.http.result.IResult;
import com.dhht.http.result.IResultCallBack;
import com.dhht.http.result.Result;
import com.dhht.task.LfTask;

/**
 * 执行后台任务
 */
public abstract class DoBackstageTask<T> extends LfTask<IResult<T>> implements IResultCallBack<T> {

    @Override
    public void onComplete(IResult<T> iResult) {
        if (iResult != null) {
            if (iResult.isSuccess()) {
                onSuccess(iResult);
            } else {
                onFailed(Result.failed(Result.CODE_505)); // 1次失败
            }
        } else {
            onFailed(Result.failed(Result.CODE_404)); //2次失败
        }
    }

    @Override
    public void onFailed(IResult t) {
        switch (t.getCode()) {
            // 可以做成统一错误码的处理
            case Result.CODE_404:
                break;
                case Result.CODE_504:
                break;
        }
    }

    @Override
    public void onException(Throwable throwable) {
        onFailed(Result.failed(Result.CODE_504)); //3次失败
    }
}
