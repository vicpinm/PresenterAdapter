package com.vicpin.presenteradapter.sample.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vicpin.presenteradapter.PresenterAdapter;
import com.vicpin.presenteradapter.SimplePresenterAdapter;
import com.vicpin.presenteradapter.ViewHolder;
import com.vicpin.presenteradapter.listeners.ItemClickListener;
import com.vicpin.presenteradapter.listeners.ItemLongClickListener;
import com.vicpin.presenteradapter.listeners.OnLoadMoreListener;
import com.vicpin.presenteradapter.model.ViewInfo;
import com.vicpin.presenteradapter.sample.R;
import com.vicpin.presenteradapter.sample.model.Country;
import com.vicpin.presenteradapter.sample.model.CountryRepository;
import com.vicpin.presenteradapter.sample.view.adapter.CountryView;
import com.vicpin.presenteradapter.sample.view.adapter.HeaderView;
import com.vicpin.presenteradapter.sample.view.interfaces.ItemDeletedListener;
import com.vicpin.presenteradapter.sample.view.interfaces.ItemRecycledListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Victor on 25/06/2016.
 */
public class MainFragment extends Fragment implements ItemClickListener<Country>,ItemLongClickListener<Country>, ItemRecycledListener, ItemDeletedListener<Country>, OnLoadMoreListener {

    @BindView(R.id.list)
    RecyclerView mList;

    @BindView(R.id.lastPresenterDestroyed)
    TextView lastPresenterDestroyed;

    private int lastPresentersRecycled;
    private int currentPage;
    private PresenterAdapter<Country> adapter;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAdapter();
        appendListeners();
        setupRecyclerView();
    }

    public void setupAdapter(){
        List<Country> data = CountryRepository.getItemsPage(getResources(), 0);
        adapter = SimplePresenterAdapter.with(CountryView.class).setLayout(R.layout.adapter_country).setData(data);
        adapter.addHeader(ViewInfo.with(HeaderView.class).setLayout(R.layout.adapter_header));
        adapter.enableLoadMore(this);
    }

    public void appendListeners(){
        adapter.setItemClickListener(this);
        adapter.setItemLongClickListener(this);
        adapter.setCustomListener(this);
    }

    public void setupRecyclerView(){
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(adapter);
    }

    @Override public void onItemClick(Country item, ViewHolder<Country> view) {
        Toast.makeText(getActivity(), "Country clicked: " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onItemLongClick(Country item, ViewHolder<Country> view) {
        Toast.makeText(getActivity(), "Country long pressed: " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onItemRecycled(int presenterId) {
        lastPresenterDestroyed.setText("Last presenters recycled: " + lastPresentersRecycled + " - " + presenterId);
        lastPresentersRecycled = presenterId;
    }

    /**
     * Pagination listener. Simulates a 1500ms load delay.
     */
    @Override public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                currentPage++;
                List<Country> newData = CountryRepository.getItemsPage(getResources(), currentPage);
                if(newData.size() > 0) {
                    adapter.addData(newData);
                } else{
                    adapter.disableLoadMore();
                }
            }
        },1500);

    }

    @Override public void onItemDeleted(Country item) {
        adapter.removeItem(item);
        adapter.updateHeaders();
    }
}
