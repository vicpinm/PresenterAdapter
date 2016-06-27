package com.vicpin.presenteradapter.sample.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vicpin.presenteradapter.PresenterAdapter;
import com.vicpin.presenteradapter.SimplePresenterAdapter;
import com.vicpin.presenteradapter.sample.R;
import com.vicpin.presenteradapter.sample.model.Country;
import com.vicpin.presenteradapter.sample.model.CountryRepository;
import com.vicpin.presenteradapter.sample.view.adapter.CountryView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Victor on 25/06/2016.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.list)
    RecyclerView mList;

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Country> data = CountryRepository.getItems(getResources());
        PresenterAdapter<Country> adapter = SimplePresenterAdapter.with(CountryView.class).setLayout(R.layout.adapter_country).setData(data);

        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(adapter);
    }
}
