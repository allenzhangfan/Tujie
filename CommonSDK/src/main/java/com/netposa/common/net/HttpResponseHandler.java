package com.netposa.common.net;

import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.utils.ArmsUtils;
import com.netposa.common.R;
import com.netposa.common.core.RouterHub;
import com.netposa.common.entity.HttpResponseEntity;
import com.netposa.common.exception.BadResultException;
import com.netposa.common.exception.EmptyResultException;
import com.netposa.common.exception.NullMessageException;
import com.netposa.common.utils.Utils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author: yeguoqiang
 * created on: 2018/8/17 13:52
 * description:
 */
@SuppressWarnings("Convert2Lambda")
public class HttpResponseHandler {

    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<HttpResponseEntity<T>, T> handleResult() {

        return new ObservableTransformer<HttpResponseEntity<T>, T>() {
            @Override
            public Observable<T> apply(Observable<HttpResponseEntity<T>> upstream) {
                return upstream.flatMap(new Function<HttpResponseEntity<T>, ObservableSource<T>>() {
                    @Override
                    public Observable<T> apply(HttpResponseEntity<T> result) throws Exception {
                        if (result.isSuccess()) {
                            if (null == result.data) {
                                return Observable.error(new EmptyResultException());
                            } else {
                                return createData(result.data);
                            }
                        } else if (result.isOutOfDate()) {//token过期
                            //token过期统一跳转登录界面
                            ARouter.getInstance().build(RouterHub.LOGIN_ACTIVITY).navigation(Utils.getContext());
                            ArmsUtils.snackbarText(Utils.getContext().getString(R.string.token_invalid));
                            return Observable.error(new TokenInvalidException(Utils.getContext().getString(R.string.token_invalid)));
                        } else if (result.isKickOff()) {//账号在另一个手机端登录被踢下线
                            //统一跳转登录界面
                            ARouter.getInstance().build(RouterHub.LOGIN_ACTIVITY).navigation(Utils.getContext());
                            ArmsUtils.snackbarText(Utils.getContext().getString(R.string.kick_off));
                            return Observable.error(new KickOffException(Utils.getContext().getString(R.string.kick_off)));
                        } else {
                            if (TextUtils.isEmpty(result.message)) {
                                return Observable.error(new NullMessageException());
                            } else {
                                return Observable.error(new BadResultException(result.message));
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };

    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(data);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }
}
