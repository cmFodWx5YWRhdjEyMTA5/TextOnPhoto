package com.dev.signatureonphoto.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "QUOTE".
*/
public class QuoteDao extends AbstractDao<Quote, Long> {

    public static final String TABLENAME = "QUOTE";

    /**
     * Properties of entity Quote.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Content = new Property(1, String.class, "content", false, "CONTENT");
        public final static Property Categore = new Property(2, String.class, "categore", false, "CATEGORE");
        public final static Property Source = new Property(3, String.class, "source", false, "SOURCE");
        public final static Property Upload = new Property(4, String.class, "upload", false, "UPLOAD");
    }


    public QuoteDao(DaoConfig config) {
        super(config);
    }
    
    public QuoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"QUOTE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CONTENT\" TEXT NOT NULL ," + // 1: content
                "\"CATEGORE\" TEXT NOT NULL ," + // 2: categore
                "\"SOURCE\" TEXT NOT NULL ," + // 3: source
                "\"UPLOAD\" TEXT NOT NULL );"); // 4: upload
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"QUOTE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Quote entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getContent());
        stmt.bindString(3, entity.getCategore());
        stmt.bindString(4, entity.getSource());
        stmt.bindString(5, entity.getUpload());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Quote entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getContent());
        stmt.bindString(3, entity.getCategore());
        stmt.bindString(4, entity.getSource());
        stmt.bindString(5, entity.getUpload());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Quote readEntity(Cursor cursor, int offset) {
        Quote entity = new Quote( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // content
            cursor.getString(offset + 2), // categore
            cursor.getString(offset + 3), // source
            cursor.getString(offset + 4) // upload
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Quote entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setContent(cursor.getString(offset + 1));
        entity.setCategore(cursor.getString(offset + 2));
        entity.setSource(cursor.getString(offset + 3));
        entity.setUpload(cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Quote entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Quote entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Quote entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
