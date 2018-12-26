package com.netposa.component.ytst.di.module;

import android.content.Context;

import com.jess.arms.di.scope.ActivityScope;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;
import com.netposa.common.utils.SizeUtils;
import com.netposa.commonres.widget.Dialog.LoadingDialogFragment;
import com.netposa.commonres.widget.Dialog.LottieDialogFragment;
import com.netposa.commonres.widget.itemDecoration.SpacesItemDecoration;
import com.netposa.component.ytst.mvp.contract.SearchResultContract;
import com.netposa.component.ytst.mvp.model.SearchResultModel;
import com.netposa.component.ytst.mvp.model.entity.ImgSearchResponseEntity;
import com.netposa.component.ytst.mvp.ui.adapter.SearchResultAdapter;

import java.util.ArrayList;


@Module
public class SearchResultModule {
    private SearchResultContract.View view;
    private static final int APP_SPAN_COUNT = 3;
    private Context mContext;
    /**
     * 构建SearchResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public SearchResultModule(Context context,SearchResultContract.View view) {
        mContext = context;
        this.view = view;
    }

    @ActivityScope
    @Provides
    SearchResultContract.View provideSearchResultView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    SearchResultContract.Model provideSearchResultModel(SearchResultModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    Context provideContext() {
        return mContext;
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
        int itemMargin = SizeUtils.dp2px(8); // 每个item与左上右下的间距
        int rvMargin = SizeUtils.dp2px(16);//整个RecyclerView与上下左右两侧的间距
        return new SpacesItemDecoration(itemMargin,itemMargin,rvMargin,rvMargin);
    }

    @ActivityScope
    @Provides
    SearchResultAdapter provideSfjbCompareResultAdapter(ArrayList<ImgSearchResponseEntity.DataBean> list) {
        return new SearchResultAdapter(mContext,list);
    }


    @ActivityScope
    @Provides
    ArrayList<ImgSearchResponseEntity.DataBean> provideBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    LottieDialogFragment provideDialogFragment() {
        return new LoadingDialogFragment();
    }

}