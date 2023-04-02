package com.luoyang.llyreader.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.luoyang.llyreader.R;
import com.luoyang.llyreader.activity.LocalImportBookActivity;
import com.luoyang.llyreader.adapter.BookShelfAdapter;
import com.luoyang.llyreader.base.MBaseFragment;
import com.luoyang.llyreader.bean.BookShelfBean;
import com.luoyang.llyreader.presenter.IMainPresenter;
import com.luoyang.llyreader.view.IMainView;
import com.luoyang.llyreader.view.MainPresenterImpl;
import com.luoyang.llyreader.widget.refresh.OnRefreshWithProgressListener;
import com.luoyang.llyreader.widget.refresh.RefreshRecyclerView;

import java.util.List;

/**
 * @author luoyang
 * @date 2023/2/22
 */
public class HomeFragment<P extends IMainPresenter> extends MBaseFragment<P> implements IMainView {

    private ImageButton ibMoney;
    private ImageButton ibLibrary;
    private ImageButton ibAdd;
    private ImageButton ibDownload;

    private RefreshRecyclerView rfRvShelf;
    private BookShelfAdapter bookShelfAdapter;

//    private DownloadListPop downloadListPop;

    @Override
    protected P initInjector() {
        return (P) new MainPresenterImpl();
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        Log.d(TAG, "主页Fragment的UI被初始化了");
        View view = View.inflate(getContext(), R.layout.fragment_home, null);
        return view;
    }

    @Override
    protected void initData() {
        bookShelfAdapter = new BookShelfAdapter();
    }

    @Override
    protected void bindView(View view) {
//        downloadListPop = new DownloadListPop(getContext());

        rfRvShelf = view.findViewById(R.id.rf_rv_shelf);

        ibLibrary = view.findViewById(R.id.ib_library);
        ibAdd = view.findViewById(R.id.ib_add);
        ibDownload = view.findViewById(R.id.ib_download);
        rfRvShelf.setRefreshRecyclerViewAdapter(bookShelfAdapter,
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void bindEvent() {
        bindRvShelfEvent();
        ibDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                downloadListPop.showAsDropDown(ibDownload);
            }
        });

        ibLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityByAnim(new Intent(getContext(), LibraryActivity.class), 0, 0);
            }
        });
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击更多
                startActivityByAnim(new Intent(getContext(), LocalImportBookActivity.class), 0, 0);
            }
        });
        bookShelfAdapter.setItemClickListener(new BookShelfAdapter.OnItemClickListener() {
            @Override
            public void toSearch() {
                //点击去选书
//                startActivityByAnim(new Intent(getContext(), LibraryActivity.class), 0, 0);
            }

            @Override
            public void onClick(BookShelfBean bookShelfBean, int index) {
//                Intent intent = new Intent(getContext(), ReadBookActivity.class);
//                intent.putExtra("from", ReadBookPresenterImpl.OPEN_FROM_APP);
//                String key = String.valueOf(System.currentTimeMillis());
//                intent.putExtra("data_key", key);
//                BitIntentDataManager.getInstance().putData(key, bookShelfBean.clone());
//                startActivityByAnim(intent, android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onLongClick(View animView, BookShelfBean bookShelfBean, int index) {
//                Intent intent = new Intent(getContext(), BookDetailActivity.class);
//                intent.putExtra("from", BookDetailPresenterImpl.FROM_BOOKSHELF);
//                String key = String.valueOf(System.currentTimeMillis());
//                intent.putExtra("data_key", key);
//                BitIntentDataManager.getInstance().putData(key, bookShelfBean);
//                startActivityByAnim(intent, animView, "img_cover", android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

    }

    private void bindRvShelfEvent() {
        rfRvShelf.setBaseRefreshListener(new OnRefreshWithProgressListener() {
            @Override
            public int getMaxProgress() {
                return bookShelfAdapter.getBooks().size();
            }

            @Override
            public void startRefresh() {
                mPresenter.queryBookShelf(true);
            }
        });
    }

    @Override
    protected void firstRequest() {
        mPresenter.queryBookShelf(false);
    }


    @Override
    public void refreshBookShelf(List<BookShelfBean> bookShelfBeanList) {
        bookShelfAdapter.replaceAll(bookShelfBeanList);
    }

    @Override
    public void activityRefreshView() {
        //执行刷新响应
        rfRvShelf.startRefresh();
    }

    @Override
    public void refreshFinish() {
        rfRvShelf.finishRefresh(false, true);
    }

    @Override
    public void refreshError(String error) {
        refreshFinish();
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshRecyclerViewItemAdd() {
        rfRvShelf.getRpb().setDurProgress(rfRvShelf.getRpb().getDurProgress() + 1);
    }

    @Override
    public void setRecyclerMaxProgress(int x) {
        rfRvShelf.getRpb().setMaxProgress(x);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        downloadListPop.onDestroy();
    }

}
