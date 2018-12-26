package com.netposa.common.utils;

import android.app.Activity;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.integration.cache.Cache;
import com.netposa.common.log.Log;

import java.lang.reflect.Field;
import java.util.Map;

import androidx.drawerlayout.widget.DrawerLayout;
import dagger.Lazy;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;

/**
 * Created by yexiaokang on 2018/11/1.
 */
@SuppressWarnings({"TryWithIdenticalCatches", "unchecked"})
public final class ReflectUtils {

    private static final String TAG = "ReflectUtils";


    /**
     * 通过反射的方法更新全局唯一的<tt>Retrofit</tt>类中的<tt>baseUrl</tt>字段
     *
     * @param repositoryManager 仓库管理器，从该管理器中获取全局唯一的<tt>Retrofit</tt>单例类
     * @param baseUrl           待更新的<tt>baseUrl</tt>
     * @param clearCache        是否清空之前的创建的<tt>Retrofit Service</tt>缓存
     * @see AppComponent#repositoryManager()
     * @see com.jess.arms.integration.RepositoryManager
     */
    public static void setRetrofitBaseUrlTemporarily(IRepositoryManager repositoryManager,
                                                     String baseUrl, boolean clearCache) {
        Retrofit retrofit = null;
        try {
            Class<?> repositoryManagerClass = repositoryManager.getClass();

            Field retrofitLazyField = repositoryManagerClass.getDeclaredField("mRetrofit");
            retrofitLazyField.setAccessible(true);
            Lazy<Retrofit> retrofitLazy = (Lazy<Retrofit>) retrofitLazyField.get(repositoryManager);
            retrofit = retrofitLazy.get();

            if (clearCache) {
                // Arms RepositoryManager Inner field
                Field retrofitServiceCacheField = repositoryManagerClass.getDeclaredField("mRetrofitServiceCache");
                retrofitServiceCacheField.setAccessible(true);
                Cache cache = (Cache) retrofitServiceCacheField.get(repositoryManager);
                if (cache != null) {
                    Log.i(TAG, "setRetrofitBaseUrlTemporarily: " + cache.toString());
                    cache.clear();
                }

                // Retrofit Inner field
                if (retrofit != null) {
                    Field serviceMethodCacheField = Retrofit.class.getDeclaredField("serviceMethodCache");
                    serviceMethodCacheField.setAccessible(true);
                    Map map = (Map) serviceMethodCacheField.get(retrofit);
                    if (map != null) {
                        map.clear();
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (retrofit != null) {
            Log.i(TAG, "setRetrofitBaseUrlTemporarily baseUrl = " + baseUrl);
            try {
                Field baseUrlField = Retrofit.class.getDeclaredField("baseUrl");
                baseUrlField.setAccessible(true);
                baseUrlField.set(retrofit, HttpUrl.parse(baseUrl));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Log.w(TAG, "setRetrofitBaseUrlTemporarily: retrofit == null");
        }
    }

    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mMinDrawerMargin");//Right
            leftDraggerField.setAccessible(true);
            int margin = leftDraggerField.getInt(drawerLayout);
            Log.i(TAG, "margin = " + margin);
            leftDraggerField.set(drawerLayout, 1);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }
}
