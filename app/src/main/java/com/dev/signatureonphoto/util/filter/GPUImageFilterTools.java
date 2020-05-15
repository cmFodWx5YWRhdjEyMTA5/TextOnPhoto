package com.dev.signatureonphoto.util.filter;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.InputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;

public class GPUImageFilterTools {
    private Context context;
    private GPUImageToneCurveFilter cloud;
    private GPUImageFilter gpuImageFilter;
    private GPUImage gpuImage;
    public GPUImageFilterTools(Context context) {
        this.context = context;
        gpuImage =  new GPUImage(context);
    }
    public void filterImage(Bitmap bitmap, InputStream inputStream) {
        cloud = new GPUImageToneCurveFilter();
        cloud.setFromCurveFileInputStream(inputStream);
        gpuImageFilter = cloud;
        gpuImage.setImage(bitmap);
        if(gpuImageFilter!=null){
            gpuImage.setFilter(gpuImageFilter);
        }
    }

    public Bitmap getBitmap() {
           return gpuImage.getBitmapWithFilterApplied();
    }
//    private GPUImageFilter createFilterForType(String filterName) {
//        switch (filterName) {
//            case B1:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.blue_oppies));
//                return cloud;
//            case B2:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.blue_yellow_field));
//                return cloud;
//            case B3:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.cold_desert));
//                return cloud;
//            case B4:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.cold_heart));
//                return cloud;
//            case B5:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.digital_film));
//                return cloud;
//            case B6:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.document_ary));
//                return cloud;
//            case B7:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.ghosts_in_your_head));
//                return cloud;
//            case B8:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.good_luck_charm));
//                return cloud;
//            case B9:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.green_envy));
//                return cloud;
//            case C1:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.humming_birds));
//                return cloud;
//            case C2:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.kiss_kiss));
//                return cloud;
//            case C3:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.lullabye));
//                return cloud;
//            case C4:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.moth_wings));
//                return cloud;
//            case C5:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.old_postcards_1));
//                return cloud;
//            case C6:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.old_postcards_2));
//                return cloud;
//            case C7:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.c7));
//                return cloud;
//            case C8:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.c8));
//                return cloud;
//            case C9:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.c9));
//                return cloud;
//            case D1:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.d1));
//                return cloud;
//            case D2:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.d2));
//                return cloud;
//            case D3:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.d3));
//                return cloud;
//            case D4:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.d4));
//                return cloud;
//            case D5:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.d5));
//                return cloud;
//            case D6:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.d6));
//                return cloud;
//            case D7:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.d7));
//                return cloud;
//            case E1:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.afterglow));
//                return cloud;
//            case E2:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.alice_wonderland));
//                return cloud;
//            case E3:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.ambers));
//                return cloud;
//            case E4:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.august_march));
//                return cloud;
//            case E5:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.aurora));
//                return cloud;
//            case E6:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.peacock_feathers));
//                return cloud;
//            case E7:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.pistol));
//                return cloud;
//            case E8:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.rose_thorns_one));
//                return cloud;
//            case E9:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.rose_thorns_two));
//                return cloud;
//            case F1:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.set_you_free));
//                return cloud;
//            case F2:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.snow_white));
//                return cloud;
//            case F3:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.wild_at_heart));
//                return cloud;
//            case F4:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(context.getResources().openRawResource(R.raw.window_warmth));
//                return cloud;
//            default:
//                cloud = new GPUImageToneCurveFilter();
//                cloud.setFromCurveFileInputStream(
//                        context.getResources().openRawResource(R.raw.blue_oppies));
//                return cloud;
//        }
//    }
}
