package com.mcba.comandaclient.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.mcba.comandaclient.R;
import com.mcba.comandaclient.model.Comanda;
import com.mcba.comandaclient.model.ComandaItem;
import com.mcba.comandaclient.presenter.ComandaSearchPresenter;
import com.mcba.comandaclient.presenter.ComandaSearchPresenterImpl;
import com.mcba.comandaclient.ui.adapter.ComandaSearchAdapter;

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by mac on 24/07/2017.
 */

public class ComandaSearchActivity extends MainSearchActivity implements ComandaSearchView, ComandaSearchAdapter.AdapterCallbacks {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerview;
    private TextView mSearchHint;
    private ComandaSearchAdapter mAdapter;

    private ComandaSearchPresenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_comanda);

        mPresenter = new ComandaSearchPresenterImpl(this);
        mPresenter.attachView();

        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_comandas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ComandaSearchAdapter(this, this);
        mRecyclerview.setAdapter(mAdapter);

        mSearchHint = (TextView) findViewById(R.id.txt_hint);
        mSearchHint.setText("Nº Comanda");


        mPresenter.fetchComandas();

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public void onComandasFetched(RealmList<Comanda> listComandas) {

        mAdapter.setItems(listComandas);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemsFetched(RealmList<ComandaItem> items, int id, double cantBultos, double total, double senia, long timestamp) {
        startActivity(ComandaSearchDetailActivity.getNewIntent(this, new ArrayList(items), id, cantBultos, total, senia, timestamp));
    }

    @Override
    public void onComandasByIdFetched(RealmResults<Comanda> listComandas) {

        if (listComandas.size() > 0) {
            mAdapter.setItems(listComandas);
            mAdapter.notifyDataSetChanged();

        }
    }

    public static Intent getNewIntent(Context context) {
        return new Intent(context, ComandaSearchActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void searchByTerm(String searchTerm) {

        if (!searchTerm.equals("")) {
            mPresenter.fetchComandasById(Integer.valueOf(searchTerm));
        }
    }

    @Override
    public void setSearchHint(MaterialSearchView searchView) {
        searchView.setHint("Nº comanda");
    }

    @Override
    public void getProgressSearch(ProgressBar progressSearch) {

    }

    @Override
    public void setOnSearchviewClose() {
        mPresenter.fetchComandas();
    }

    @Override
    public void setInputType(MaterialSearchView searchView) {
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_description
            );
            mToolbar.setNavigationIcon(drawable);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //open activity client
                }
            });
            setSupportActionBar(mToolbar);
            setupActionBar(getSupportActionBar());
        }
    }

    private void setupActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onItemPress(Comanda comanda) {

        mPresenter.fetchItems(comanda.comandaId, comanda.cantBultos, comanda.mTotal, comanda.mSenia, comanda.timestamp);

    }
}
