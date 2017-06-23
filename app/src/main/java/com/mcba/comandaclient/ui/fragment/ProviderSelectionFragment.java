package com.mcba.comandaclient.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mcba.comandaclient.R;
import com.mcba.comandaclient.model.Product;
import com.mcba.comandaclient.model.Provider;
import com.mcba.comandaclient.model.ProviderList;
import com.mcba.comandaclient.presenter.ProductListPresenter;
import com.mcba.comandaclient.presenter.ProductListPresenterImpl;
import com.mcba.comandaclient.ui.ProductsListView;
import com.mcba.comandaclient.ui.adapter.ProviderSelectionAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by mac on 21/06/2017.
 */

public class ProviderSelectionFragment extends BaseNavigationFragment<ProviderSelectionFragment.ProviderSelectionFragmentCallbacks> implements ProductsListView, ProviderSelectionAdapter.AdapterCallbacks {

    public static final String PRODUCT_ID = "productId";

    private RecyclerView mRecyclerview;
    private ProviderSelectionAdapter mAdapter;
    private ProductListPresenter mPresenter;
    private int mProductId;


    public static ProviderSelectionFragment newInstance(int productId) {

        Bundle args = new Bundle();
        args.putInt(PRODUCT_ID, productId);
        ProviderSelectionFragment fragment = new ProviderSelectionFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.provider_selection_fragment;
    }

    @Override
    protected void setViewReferences() {

        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_provider_selection);

    }

    @Override
    protected void setupFragment(Bundle savedInstanceState) {

        mPresenter = new ProductListPresenterImpl(this);
        mPresenter.attachView();

        mAdapter = new ProviderSelectionAdapter(getActivity(), this);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProductId = getArguments().getInt(PRODUCT_ID);

        mPresenter.getProducts();
    }

    @Override
    public void onItemPress(Provider provider) {

        mCallbacks.onGoToSelectProductType(provider.productId);
    }

    @Override
    public void showDataResponse(RealmList<ProviderList> providers, RealmList<Product> products) {

        mPresenter.parseProviders(providers, mProductId);

    }

    @Override
    public void showProvidersResponse(List<Provider> providers) {

        mAdapter.setItems(providers);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponseFailed() {

    }

    @Override
    public void realmStoreCompleted() {

    }

    @Override
    public void realmStoreFailed() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onItemPress() {

    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }


    public interface ProviderSelectionFragmentCallbacks {
        void onGoToSelectProductType(int providerId);
    }

    @Override
    public ProviderSelectionFragment.ProviderSelectionFragmentCallbacks getDummyCallbacks() {
        return new ProviderSelectionFragment.ProviderSelectionFragmentCallbacks() {
            @Override
            public void onGoToSelectProductType(int providerId) {

            }
        };
    }
}