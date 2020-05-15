package com.dev.signatureonphoto.features.saved;

import java.util.Comparator;


public class SortSavedDate implements Comparator<GalleryItem> {
    @Override
    public int compare(GalleryItem o1, GalleryItem o2) {
        return o2.getDate().compareTo(o1.getDate());
    }
}
