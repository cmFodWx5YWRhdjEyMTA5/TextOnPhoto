package com.dev.signatureonphoto.features.main;

import android.content.Context;

import com.dev.signatureonphoto.data.model.response.StickerItem;

import java.io.IOException;
import java.util.ArrayList;

public class StickersPreseneter {
    public static StickersPreseneter instance=null;
    public static StickersPreseneter getInstance(){
        if(instance==null){
            instance=new StickersPreseneter();
        }
        return instance;
    }
    private StickersPreseneter(){}

    public static ArrayList<StickerItem> getListSticker(Context context, String... path) {
        String[] list;
        ArrayList<StickerItem> stickersItems = new ArrayList<>();
        try {
            for (String dir : path) {
                list = context.getAssets().list(dir);
                for (String stickerName : list) {
                    stickersItems.add(new StickerItem(dir + "/" + stickerName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stickersItems;
    }
}
