package com.example.lib;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class DatabaseGenerator {
    private static final String PROJECT_DIR = System.getProperty("user.dir");
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(6, "com.eco.textonphoto.database");
        createTable(schema);
        new DaoGenerator().generateAll(schema, PROJECT_DIR+"/app/src/main/java/");
    }

    private static void createTable(Schema schema) {
        Entity quote = schema.addEntity("Quote");
        quote.addIdProperty().autoincrement();
        quote.addStringProperty("content").notNull();
        quote.addStringProperty("categore").notNull();
        quote.addStringProperty("source").notNull();
        quote.addStringProperty("upload").notNull();
    }
}
