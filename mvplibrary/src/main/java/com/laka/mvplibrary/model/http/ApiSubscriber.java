package com.laka.mvplibrary.model.http;

import android.content.Context;
import android.net.ParseException;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.laka.mvplibrary.model.exception.ApiException;

import org.json.JSONException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * @Author:summer
 * @Date:2018/12/14
 * @Description:
 */
public abstract class ApiSubscriber<T> implements Subscriber<T> {

    private Context mContext;
    // private WaitingDialog waitingDialog;  //加载dialog
    private boolean isShowWaitDialog;

    public void setShowWaitDialog(boolean showWaitDialog) {
        isShowWaitDialog = showWaitDialog;
    }

    // 订阅
    @Override
    public void onSubscribe(Subscription s) {
        if (isShowWaitDialog) {
            showWaitDialog();
        }
    }

    // 完成
    @Override
    public void onComplete() {
        if (isShowWaitDialog) {
            dismissDialog();
        }
    }

    public void setmCtx(Context mCtx) {
        this.mContext = mCtx;
    }


    /**
     * 对 onError进行处理
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (isShowWaitDialog) {
            dismissDialog();
        }
        Throwable throwable = e;
        /**
         * 获取根源 异常
         */
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        if (e instanceof HttpException) {//对网络异常 弹出相应的toast
            HttpException httpException = (HttpException) e;
            if (TextUtils.isEmpty(httpException.getMessage())) {
                // ToastUtil.showToast(mCtx, R.string.imi_toast_common_net_error);
            } else {
                String errorMsg = httpException.getMessage();
                // ToastUtil.showToast(mCtx, errorMsg);
            }
        } else if (e instanceof ApiException) {//服务器返回的错误
            onResultError((ApiException) e);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {//解析异常
           // ToastUtil.showToast(mCtx, R.string.imi_toast_common_parse_error);
        } else if (e instanceof UnknownHostException) {
           // ToastUtil.showToast(mCtx, R.string.imi_toast_common_server_error);
        } else if (e instanceof SocketTimeoutException) {
           // ToastUtil.showToast(mCtx, R.string.imi_toast_common_net_timeout);
        } else {
            e.printStackTrace();
           // ToastUtil.showToast(mCtx, R.string.imi_toast_common_net_error);
        }
    }

    /**
     * 服务器返回的错误，根据服务器定义的错误码弹出不同的提示
     *
     * @param ex
     */
    protected void onResultError(ApiException ex) {
        switch (ex.getCode()) {  //服务器返回code默认处理
            case 10021:
                // ToastUtil.showToast(mCtx, R.string.imi_login_input_mail_error);
                break;
            case 10431:
                // ToastUtil.showToast(mCtx, R.string.imi_const_tip_charge);
                break;
            default:
                String msg = ex.getMessage();
                if (TextUtils.isEmpty(msg)) {
                   // ToastUtil.showToast(mCtx, R.string.imi_toast_common_net_error);
                } else {
                   // ToastUtil.showToast(mCtx, msg);
                }
        }

    }

    private void dismissDialog() {
//        if (waitingDialog != null) {
//            if (waitingDialog.isShowing()) {
//                waitingDialog.dismiss();
//            }
//        }
    }

    private void showWaitDialog() {
//        if (waitingDialog == null) {
//            waitingDialog = new WaitingDialog(mCtx);
//            waitingDialog.setDialogWindowStyle();
//            waitingDialog.setCanceledOnTouchOutside(false);
//        }
//        waitingDialog.show();
    }

}
