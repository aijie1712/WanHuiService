package com.cmeplaza.basemodule.base.mvp;

import android.app.Dialog;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * @author AndroidAj.
 */
public interface BaseContract {

    interface BasePresenter<T extends BaseContract.BaseView> {

        void attachView(T view);

        void detachView();
    }

    interface BaseView {
        void showError(String message);

        void showProgress();

        void showProgress(String message);

        Dialog getDialog(String message);

        void hideProgress();

        <T> LifecycleTransformer<T> bind();
    }
}
