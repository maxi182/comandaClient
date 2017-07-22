package com.mcba.comandaclient.interactor;

import android.util.Log;

import com.mcba.comandaclient.model.Comanda;
import com.mcba.comandaclient.model.ComandaItem;
import com.mcba.comandaclient.model.ComandaList;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by mac on 08/07/2017.
 */

public class ComandaInteractorImpl extends RealmManager implements ComandaInteractorCallbacks {

    private RealmAsyncTask mTransaction;


    @Override
    public void fetchComandas(RequestCallback requestCallback) {

        if (isRealmDBLoaded()) {
            //  requestCallback.onFetchComandaSuccess(getRealmComandaData());
        } else {
            requestCallback.onFetchComandaFail();

        }
    }

    @Override
    public void fetchComandaItems(RequestCallback requestCallback, int id) {

            if (checkItemsByComanda(id).size() > 0) {
                requestCallback.onFetchComandaItems(getComandaItemsFromDB(id));
            }
            else{
                requestCallback.onFetchItemsFail();
            }
    }

    @Override
    public void fetchComandaById(RequestCallback requestCallback, int id) {

        if (isRealmDBLoaded()) {
            requestCallback.onFetchComandaSuccess(getComandaFromDB(id));
        } else {
            requestCallback.onFetchComandaFail();

        }
    }

    private Comanda getComandaFromDB(int id) {

        return mRealm.where(Comanda.class).equalTo("comandaId", id).findFirst();

    }

    private RealmList<ComandaItem> getComandaItemsFromDB(int id) {

        return mRealm.where(Comanda.class).equalTo("comandaId", id).findFirst().comandaItemList;

    }

    private RealmResults<ComandaItem> checkItemsByComanda(int id) {

        return mRealm.where(ComandaItem.class).equalTo("comandaId", id).findAll();

    }

    @Override
    public void storeComandas(final RequestCallback callback, final ComandaList comandas) {

        mTransaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                bgRealm.copyToRealmOrUpdate(comandas);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onStoreCompleted(true);
                // callback.onFetchDataSuccess(getRealmProviderList(), getRealmProductList());
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callback.onStoreCompleted(false);
            }
        });

    }

    @Override
    public void storeComanda(final RequestCallback callback, final Comanda comanda) {


        mTransaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                bgRealm.insertOrUpdate(comanda);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callback.onStoreCompleted(true);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callback.onStoreCompleted(false);
            }
        });

    }

    @Override
    public void getLastComandaId(RequestCallback requestCallback) {

        Number nextID = mRealm.where(Comanda.class).max("comandaId");

        if (nextID != null) {
            requestCallback.onFetchLastComandaId(nextID.intValue());
        } else {
            requestCallback.onFetchLastComandaId(0);
        }

    }


    private RealmResults<Comanda> getRealmComandaData() {

        return mRealm.where(Comanda.class).findAll();

    }

    private boolean isItemListLoaded(int id) {

        return (getComandaItemsFromDB(id) != null && !getComandaItemsFromDB(id).isEmpty()) ? true : false;
    }

    private boolean isRealmDBLoaded() {

        return (getRealmComandaData() != null && !getRealmComandaData().isEmpty()) ? true : false;

    }


    @Override
    public void attachView() {

        Log.d("LOG", "Attach View");
        initRealm();

    }

    @Override
    public void detachView() {

        Log.d("LOG", "detachView");

        if (mTransaction != null && !mTransaction.isCancelled()) {
            mTransaction.cancel();
        }
        closeRealm();

    }
}
