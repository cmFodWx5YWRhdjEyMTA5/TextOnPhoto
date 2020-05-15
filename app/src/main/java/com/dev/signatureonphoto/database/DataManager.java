package com.dev.signatureonphoto.database;

import android.content.Context;
import androidx.annotation.NonNull;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {
    @Inject
    DataManager() {
    }
    private static DaoSession mDaoSession;
    private static final boolean ENCRYPTED = true;

    private static class SingletonHolder {
        private static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
    public void init(Context context) {
        init(context, "db");
    }

    private void init(@NonNull Context context, @NonNull String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context.getApplicationContext(), ENCRYPTED ? "notes-db-encrypted" : "articles-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    private DaoSession getDaoSession() {
        if (null == mDaoSession) {
            throw new NullPointerException("green db has not been initialized");
        }
        return mDaoSession;
    }
    public static DaoSession query(){
        return DataManager.getInstance().getDaoSession();
    }
}
