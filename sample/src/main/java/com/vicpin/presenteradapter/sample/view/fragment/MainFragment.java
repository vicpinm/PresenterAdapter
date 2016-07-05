package com.vicpin.presenteradapter.sample.view.fragment;

import android.os.Bundle;
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
import com.vicpin.presenteradapter.sample.R;
import com.vicpin.presenteradapter.sample.model.Country;
import com.vicpin.presenteradapter.sample.model.CountryRepository;
import com.vicpin.presenteradapter.sample.view.adapter.CountryView;
import com.vicpin.presenteradapter.sample.view.interfaces.PresenterRecycledListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Victor on 25/06/2016.
 */
public class MainFragment extends Fragment implements ItemClickListener<Country>,ItemLongClickListener<Country>, PresenterRecycledListener {

    @BindView(R.id.list)
    RecyclerView mList;

    @BindView(R.id.lastPresenterDestroyed)
    TextView lastPresenterDestroyed;

    private int lastPresentersRecycled;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Country> data = CountryRepository.getItems(getResources());
        PresenterAdapter<Country> adapter = SimplePresenterAdapter.with(CountryView.class).setLayout(R.layout.adapter_country).setData(data);
        appendListeners(adapter);

        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(adapter);
    }

    public void appendListeners(PresenterAdapter<Country> adapter){
        adapter.setItemClickListener(this);
        adapter.setItemLongClickListener(this);
        adapter.setCustomListener(this);
    }

    @Override public void onItemClick(Country item, ViewHolder<Country> view) {
        Toast.makeText(getActivity(), "Country clicked: " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onItemLongClick(Country item, ViewHolder<Country> view) {
        Toast.makeText(getActivity(), "Country long pressed: " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onPresenterRecycled(int presenterId) {
        lastPresenterDestroyed.setText("Last presenters recycled: " + lastPresentersRecycled + " - " + presenterId);
        lastPresentersRecycled = presenterId;
    }
}
