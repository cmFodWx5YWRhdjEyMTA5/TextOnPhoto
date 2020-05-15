package com.dev.signatureonphoto.features.saved;

import java.util.ArrayList;


public interface SavedMvp {
    void loadedGallery(ArrayList<GalleryItem> listGallery);
    void itemOnClicked(String path);
}
