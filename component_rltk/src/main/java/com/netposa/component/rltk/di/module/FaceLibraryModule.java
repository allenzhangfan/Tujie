package com.netposa.component.rltk.di.module;

import android.content.Context;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;

import com.netposa.common.utils.SizeUtils;
import com.netposa.commonres.widget.CustomerLoadMoreView;
import com.netposa.commonres.widget.itemDecoration.SpacesItemDecoration;
import com.netposa.component.rltk.mvp.contract.FaceLibraryContract;
import com.netposa.component.rltk.mvp.model.FaceLibraryModel;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryRequestEntity;
import com.netposa.component.rltk.mvp.model.entity.FaceLibraryResponseEntity;
import com.netposa.component.rltk.mvp.ui.adapter.FaceLibraryAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.netposa.common.constant.GlobalConstants.PAGE_SIZE_DEFAULT;

@Module
public class FaceLibraryModule {
    private static final int APP_SPAN_COUNT = 2;
    private Context mContext;
    private FaceLibraryContract.View view;

    /**
     * 构建FaceLibraryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public FaceLibraryModule(Context context, FaceLibraryContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    FaceLibraryContract.View provideFaceLibraryView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    FaceLibraryContract.Model provideFaceLibraryModel(FaceLibraryModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
    }

    @ActivityScope
    @Provides
    LoadMoreView providerLoadmoreView() {
        return new CustomerLoadMoreView();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(mContext, APP_SPAN_COUNT, RecyclerView.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemAnimator provideItemAnimator() {
        return new DefaultItemAnimator();
    }

    @ActivityScope
    @Provides
    RecyclerView.ItemDecoration provideGridItemDecoration() {
        int itemMargin = SizeUtils.dp2px(16); // 每个item与左上右下的间距
        int rvMargin = SizeUtils.dp2px(16);//整个RecyclerView与上下左右两侧的间距
        return new SpacesItemDecoration(itemMargin, itemMargin, rvMargin,rvMargin);
    }

    @ActivityScope
    @Provides
    List<FaceLibraryResponseEntity.ListEntity> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    FaceLibraryAdapter provideFaceLibraryAdapter(List<FaceLibraryResponseEntity.ListEntity> beanList) {
        return new FaceLibraryAdapter(mContext, beanList);
    }

    @ActivityScope
    @Provides
    FaceLibraryRequestEntity provideFaceLibraryRequestEntity() {
        FaceLibraryRequestEntity requestEntity = new FaceLibraryRequestEntity();
        requestEntity.setPageSize(PAGE_SIZE_DEFAULT);
        return requestEntity;
    }
}