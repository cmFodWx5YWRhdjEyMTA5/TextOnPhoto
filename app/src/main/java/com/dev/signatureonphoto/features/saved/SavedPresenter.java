package com.dev.signatureonphoto.features.saved;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static com.dev.signatureonphoto.Constants.FOLDER_NAME;


public class SavedPresenter {

    private SavedMvp savedMvp;

    public void setSavedMvp(SavedMvp savedMvp) {
        this.savedMvp = savedMvp;
    }

    @SuppressLint("StaticFieldLeak")
    public void readImageSaved() {
        new AsyncTask<Void, Void, ArrayList<GalleryItem>>() {

            @Override
            protected ArrayList<GalleryItem> doInBackground(Void... voids) {
                ArrayList<GalleryItem> imageList = new ArrayList<>();
                String pathFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + FOLDER_NAME;
                File dir = new File(pathFolder);
                File listFile[] = dir.listFiles();
                if (listFile != null && listFile.length > 0) {
                    for (int i = 0; i < listFile.length; i++) {
                        if (listFile[i].getName().endsWith(".jpg")) {
                            File file = listFile[i];
                            File fileDate = new File(file.getPath());
                            Date lastModDate = new Date(fileDate.lastModified());
                            GalleryItem item = new GalleryItem(file.getPath(), lastModDate);
                            imageList.add(item);
                        }
                    }
                }
                return imageList;
            }

            @Override
            protected void onPostExecute(ArrayList<GalleryItem> list) {
                super.onPostExecute(list);
                savedMvp.loadedGallery(list);
            }
        }.execute();
    }
}
