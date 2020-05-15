package com.dev.signatureonphoto.features.main;

import com.dev.signatureonphoto.data.model.response.Filter;

import java.util.List;

public interface FilterListener {
    void updateItem(int position, int x);
    void onFilterSelected(String filterPath);
    void onFilterLoaded(List<List<Filter>> filters);
}
