package com.dev.signatureonphoto.util;

import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.data.model.ImageTemplate;
import com.dev.signatureonphoto.data.model.response.Color;
import com.dev.signatureonphoto.data.model.response.ColorBackground;
import com.dev.signatureonphoto.data.model.response.StickerItem;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final int PERM_RQST_CODE_CAMERA = 120;
    public static final int PERM_RQST_CODE_WRITE = 130;
    public static final int REQUEST_TEMPLATE = 1000;
    public static final int REQUEST_EDITTEXT = 2000;
    public static final int REQUEST_USER_IMAGE = 3000;
    public static final int COLOR_RESULT = 1003;
    public static final int EDITTEXT_RESULT = 1002;
    public static final String KEY_RU = "ru";
    public static boolean openTemplateEditor = false;

    public static final String CURRENT_QUALITY = "current_quality";
    public static final String ISFROMCONGRAT = "isFromCongrat";

    public static final String[] categoryNames = {"Nature", "Macro", "Light", "Love", "Life_Style"};
    public static final String NAME_EXTRA_TEMPLATE = "template";
    public static final String NAME_EXTRA_COLOR_BACKGROUND = "color_background";
    public static final String KEY_TYPE = "key_type";
    public static final String BUNDLE_TEMPLATE = "bundle_template";
    public static final String NAME_EXTRA_URI = "uri";
    private static String lang;
    public static final String KEY_VI = "vi";

    public static ArrayList<ColorBackground> getColorBackgrounds() {

        ArrayList<ColorBackground> colors = new ArrayList<>();
        colors.add(new ColorBackground(R.color.colorBackground1));
        colors.add(new ColorBackground(R.color.colorBackground2));
        colors.add(new ColorBackground(R.color.colorBackground3));
        colors.add(new ColorBackground(R.color.colorBackground4));
        colors.add(new ColorBackground(R.color.colorBackground5));
        colors.add(new ColorBackground(R.color.colorBackground6));
        colors.add(new ColorBackground(R.color.colorBackground7));
        colors.add(new ColorBackground(R.color.colorBackground8));
        colors.add(new ColorBackground(R.color.colorBackground9));
        colors.add(new ColorBackground(R.color.colorBackground10));
        colors.add(new ColorBackground(R.color.colorBackground11));
        colors.add(new ColorBackground(R.color.colorBackground12));
        colors.add(new ColorBackground(R.color.colorBackground13));
        colors.add(new ColorBackground(R.color.colorBackground14));
        colors.add(new ColorBackground(R.color.colorBackground15));
        colors.add(new ColorBackground(R.color.colorBackground16));
        colors.add(new ColorBackground(R.color.colorBackground17));
        colors.add(new ColorBackground(R.color.colorBackground18));
        colors.add(new ColorBackground(R.color.colorBackground19));
        colors.add(new ColorBackground(R.color.colorBackground20));
        colors.add(new ColorBackground(R.color.colorBackground21));
        colors.add(new ColorBackground(R.color.colorBackground22));
        colors.add(new ColorBackground(R.color.colorBackground23));
        colors.add(new ColorBackground(R.color.colorBackground24));
        colors.add(new ColorBackground(R.color.colorBackground25));
        colors.add(new ColorBackground(R.color.colorBackground26));
        colors.add(new ColorBackground(R.color.colorBackground27));
        colors.add(new ColorBackground(R.color.colorBackground28));
        colors.add(new ColorBackground(R.color.colorBackground29));
        colors.add(new ColorBackground(R.color.colorBackground30));
        colors.add(new ColorBackground(R.color.colorBackground31));

        colors.add(new ColorBackground(R.color.colorBackground51));
        colors.add(new ColorBackground(R.color.colorBackground52));
        colors.add(new ColorBackground(R.color.colorBackground53));
        colors.add(new ColorBackground(R.color.colorBackground54));
        colors.add(new ColorBackground(R.color.colorBackground55));
        colors.add(new ColorBackground(R.color.colorBackground56));
        colors.add(new ColorBackground(R.color.colorBackground57));
        colors.add(new ColorBackground(R.color.colorBackground58));
        colors.add(new ColorBackground(R.color.colorBackground59));
        colors.add(new ColorBackground(R.color.colorBackground60));
        colors.add(new ColorBackground(R.color.colorBackground62));
        colors.add(new ColorBackground(R.color.colorBackground63));
        colors.add(new ColorBackground(R.color.colorBackground64));
        colors.add(new ColorBackground(R.color.colorBackground65));
        colors.add(new ColorBackground(R.color.colorBackground66));
        colors.add(new ColorBackground(R.color.colorBackground67));
        colors.add(new ColorBackground(R.color.colorBackground68));
        colors.add(new ColorBackground(R.color.colorBackground69));
        colors.add(new ColorBackground(R.color.colorBackground70));
        colors.add(new ColorBackground(R.color.colorBackground71));
        colors.add(new ColorBackground(R.color.colorBackground72));
        colors.add(new ColorBackground(R.color.colorBackground73));
        colors.add(new ColorBackground(R.color.colorBackground74));
        colors.add(new ColorBackground(R.color.colorBackground75));
        colors.add(new ColorBackground(R.color.colorBackground76));
        colors.add(new ColorBackground(R.color.colorBackground77));
        colors.add(new ColorBackground(R.color.colorBackground78));
        colors.add(new ColorBackground(R.color.colorBackground79));
        colors.add(new ColorBackground(R.color.colorBackground80));
        colors.add(new ColorBackground(R.color.colorBackground81));
        colors.add(new ColorBackground(R.color.colorBackground82));
        colors.add(new ColorBackground(R.color.colorBackground83));
        colors.add(new ColorBackground(R.color.colorBackground84));
        colors.add(new ColorBackground(R.color.colorBackground85));
        colors.add(new ColorBackground(R.color.colorBackground86));
        colors.add(new ColorBackground(R.color.colorBackground87));
        colors.add(new ColorBackground(R.color.colorBackground88));
        colors.add(new ColorBackground(R.color.colorBackground89));
        colors.add(new ColorBackground(R.color.colorBackground90));
        colors.add(new ColorBackground(R.color.colorBackground91));
        colors.add(new ColorBackground(R.color.colorBackground92));
        colors.add(new ColorBackground(R.color.colorBackground93));
        colors.add(new ColorBackground(R.color.colorBackground94));
        colors.add(new ColorBackground(R.color.colorBackground95));
        colors.add(new ColorBackground(R.color.colorBackground96));
        colors.add(new ColorBackground(R.color.colorBackground97));
        colors.add(new ColorBackground(R.color.colorBackground98));
        return colors;
    }


    public static ArrayList<ImageTemplate> getTemplate() {

        ArrayList<ImageTemplate> imageTemplates = new ArrayList<>();


        return imageTemplates;
    }

    public static ArrayList<StickerItem> getImageSticker() {

        ArrayList<StickerItem> stickers = new ArrayList<>();

        return stickers;
    }

    public static ArrayList<StickerItem> getImageStickerPreview() {

        ArrayList<StickerItem> stickers = new ArrayList<>();

        return stickers;
    }

    public static ArrayList<StickerItem> getXmasSticker() {
        ArrayList<StickerItem> stickers = new ArrayList<>();

        return stickers;
    }

    public static String[] getFontVN = new String[]{
            "f25_agency.ttf", "f26_effra.ttf", "f27_merge.ttf", "f28_servetica.ttf",
            "f29_golf.ttf", "f30_fairview.ttf", "f31_mussica.ttf", "f32_sneaker.ttf", "f33_angelline.ttf", "f34_matilde.ttf",
            "f35_semilla.ttf", "f02_hipsteria.ttf", "f03_iciel la chic.ttf", "f04_auther.ttf", "f05_butcherandblock.ttf",
            "f06_helena.ttf", "f07_iciel smoothy.ttf", "f08_iciel showcase.ttf",
            "f09_iciel stabile.ttf", "f10_brawls1.ttf", "f11_humblehearts.ttf",
            "f12_iciel stringfellow.ttf", "f13_iciel bambola.ttf", "f14_blooming_elegant_hand.ttf", "f15_iciel crocante.ttf", "f16_blooming_elegant.ttf",
            "f17_cucho.ttf", "f18_gotham_thin.ttf", "f19_fairy_tales.ttf", "f20_mijas_ultra.ttf", "f21_novecento_sans.ttf",
            "f22_fetridge.ttf", "f23_panton-light.ttf", "f24_panton_light_italic.ttf"
    };

    public static ArrayList<Color> getColorText() {

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(R.color.colorBackground1));
        colors.add(new Color(R.color.colorBackground2));
        colors.add(new Color(R.color.colorBackground3));
        colors.add(new Color(R.color.colorBackground4));
        colors.add(new Color(R.color.colorBackground5));
        colors.add(new Color(R.color.colorBackground6));
        colors.add(new Color(R.color.colorBackground7));
        colors.add(new Color(R.color.colorBackground8));
        colors.add(new Color(R.color.colorBackground9));
        colors.add(new Color(R.color.colorBackground10));
        colors.add(new Color(R.color.colorBackground11));
        colors.add(new Color(R.color.colorBackground12));

        colors.add(new Color(R.color.colorBackground32));
        colors.add(new Color(R.color.colorBackground33));
        colors.add(new Color(R.color.colorBackground34));
        colors.add(new Color(R.color.colorBackground35));
        colors.add(new Color(R.color.colorBackground36));
        colors.add(new Color(R.color.colorBackground37));
        colors.add(new Color(R.color.colorBackground38));
        colors.add(new Color(R.color.colorBackground39));
        colors.add(new Color(R.color.colorBackground40));
        colors.add(new Color(R.color.colorBackground41));
        colors.add(new Color(R.color.colorBackground42));
        colors.add(new Color(R.color.colorBackground43));
        colors.add(new Color(R.color.colorBackground44));
        colors.add(new Color(R.color.colorBackground45));
        colors.add(new Color(R.color.colorBackground46));
        colors.add(new Color(R.color.colorBackground47));
        colors.add(new Color(R.color.colorBackground48));
        colors.add(new Color(R.color.colorBackground49));
        colors.add(new Color(R.color.colorBackground50));

        colors.add(new Color(R.color.colorBackground51));
        colors.add(new Color(R.color.colorBackground52));
        colors.add(new Color(R.color.colorBackground53));
        colors.add(new Color(R.color.colorBackground54));
        colors.add(new Color(R.color.colorBackground55));
        colors.add(new Color(R.color.colorBackground56));
        colors.add(new Color(R.color.colorBackground57));
        colors.add(new Color(R.color.colorBackground58));
        colors.add(new Color(R.color.colorBackground59));
        colors.add(new Color(R.color.colorBackground60));
        colors.add(new Color(R.color.colorBackground62));
        colors.add(new Color(R.color.colorBackground63));
        colors.add(new Color(R.color.colorBackground64));
        colors.add(new Color(R.color.colorBackground65));
        colors.add(new Color(R.color.colorBackground66));
        colors.add(new Color(R.color.colorBackground67));
        colors.add(new Color(R.color.colorBackground68));
        colors.add(new Color(R.color.colorBackground69));
        colors.add(new Color(R.color.colorBackground70));
        colors.add(new Color(R.color.colorBackground71));
        colors.add(new Color(R.color.colorBackground72));
        colors.add(new Color(R.color.colorBackground73));
        colors.add(new Color(R.color.colorBackground74));
        colors.add(new Color(R.color.colorBackground75));
        colors.add(new Color(R.color.colorBackground76));
        colors.add(new Color(R.color.colorBackground77));
        colors.add(new Color(R.color.colorBackground78));
        colors.add(new Color(R.color.colorBackground79));
        colors.add(new Color(R.color.colorBackground80));
        colors.add(new Color(R.color.colorBackground81));
        colors.add(new Color(R.color.colorBackground82));
        colors.add(new Color(R.color.colorBackground83));
        colors.add(new Color(R.color.colorBackground84));
        colors.add(new Color(R.color.colorBackground85));
        colors.add(new Color(R.color.colorBackground86));
        colors.add(new Color(R.color.colorBackground87));
        colors.add(new Color(R.color.colorBackground88));
        colors.add(new Color(R.color.colorBackground89));
        colors.add(new Color(R.color.colorBackground90));
        colors.add(new Color(R.color.colorBackground91));
        colors.add(new Color(R.color.colorBackground92));
        colors.add(new Color(R.color.colorBackground93));
        colors.add(new Color(R.color.colorBackground94));
        colors.add(new Color(R.color.colorBackground95));
        colors.add(new Color(R.color.colorBackground96));
        colors.add(new Color(R.color.colorBackground97));
        colors.add(new Color(R.color.colorBackground98));

        return colors;
    }

    public static ArrayList<Color> getColorTextShadow() {

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(R.drawable.ic_none));
        colors.add(new Color(R.color.colorBackground1));
        colors.add(new Color(R.color.colorBackground2));
        colors.add(new Color(R.color.colorBackground3));
        colors.add(new Color(R.color.colorBackground4));
        colors.add(new Color(R.color.colorBackground5));
        colors.add(new Color(R.color.colorBackground6));
        colors.add(new Color(R.color.colorBackground7));
        colors.add(new Color(R.color.colorBackground8));
        colors.add(new Color(R.color.colorBackground9));
        colors.add(new Color(R.color.colorBackground10));
        colors.add(new Color(R.color.colorBackground11));
        colors.add(new Color(R.color.colorBackground12));

        colors.add(new Color(R.color.colorBackground51));
        colors.add(new Color(R.color.colorBackground52));
        colors.add(new Color(R.color.colorBackground53));
        colors.add(new Color(R.color.colorBackground54));
        colors.add(new Color(R.color.colorBackground55));
        colors.add(new Color(R.color.colorBackground56));
        colors.add(new Color(R.color.colorBackground57));
        colors.add(new Color(R.color.colorBackground58));
        colors.add(new Color(R.color.colorBackground59));
        colors.add(new Color(R.color.colorBackground60));
        colors.add(new Color(R.color.colorBackground62));
        colors.add(new Color(R.color.colorBackground63));
        colors.add(new Color(R.color.colorBackground64));
        colors.add(new Color(R.color.colorBackground65));
        colors.add(new Color(R.color.colorBackground66));
        colors.add(new Color(R.color.colorBackground67));
        colors.add(new Color(R.color.colorBackground68));
        colors.add(new Color(R.color.colorBackground69));
        colors.add(new Color(R.color.colorBackground70));
        colors.add(new Color(R.color.colorBackground71));
        colors.add(new Color(R.color.colorBackground72));
        colors.add(new Color(R.color.colorBackground73));
        colors.add(new Color(R.color.colorBackground74));
        colors.add(new Color(R.color.colorBackground75));
        colors.add(new Color(R.color.colorBackground76));
        colors.add(new Color(R.color.colorBackground77));
        colors.add(new Color(R.color.colorBackground78));
        colors.add(new Color(R.color.colorBackground79));
        colors.add(new Color(R.color.colorBackground80));
        colors.add(new Color(R.color.colorBackground81));
        colors.add(new Color(R.color.colorBackground82));
        colors.add(new Color(R.color.colorBackground83));
        colors.add(new Color(R.color.colorBackground84));
        colors.add(new Color(R.color.colorBackground85));
        colors.add(new Color(R.color.colorBackground86));
        colors.add(new Color(R.color.colorBackground87));
        colors.add(new Color(R.color.colorBackground88));
        colors.add(new Color(R.color.colorBackground89));
        colors.add(new Color(R.color.colorBackground90));
        colors.add(new Color(R.color.colorBackground91));
        colors.add(new Color(R.color.colorBackground92));
        colors.add(new Color(R.color.colorBackground93));
        colors.add(new Color(R.color.colorBackground94));
        colors.add(new Color(R.color.colorBackground95));
        colors.add(new Color(R.color.colorBackground96));
        colors.add(new Color(R.color.colorBackground97));
        colors.add(new Color(R.color.colorBackground98));
        return colors;
    }

    public static ArrayList<Color> getColorTextBorder() {

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(new Color(R.color.colorBackground1));
        colors.add(new Color(R.color.colorBackground2));
        colors.add(new Color(R.color.colorBackground3));
        colors.add(new Color(R.color.colorBackground4));
        colors.add(new Color(R.color.colorBackground5));
        colors.add(new Color(R.color.colorBackground6));
        colors.add(new Color(R.color.colorBackground7));
        colors.add(new Color(R.color.colorBackground8));
        colors.add(new Color(R.color.colorBackground9));
        colors.add(new Color(R.color.colorBackground10));
        colors.add(new Color(R.color.colorBackground11));
        colors.add(new Color(R.color.colorBackground12));

        colors.add(new Color(R.color.colorBackground51));
        colors.add(new Color(R.color.colorBackground52));
        colors.add(new Color(R.color.colorBackground53));
        colors.add(new Color(R.color.colorBackground54));
        colors.add(new Color(R.color.colorBackground55));
        colors.add(new Color(R.color.colorBackground56));
        colors.add(new Color(R.color.colorBackground57));
        colors.add(new Color(R.color.colorBackground58));
        colors.add(new Color(R.color.colorBackground59));
        colors.add(new Color(R.color.colorBackground60));
        colors.add(new Color(R.color.colorBackground62));
        colors.add(new Color(R.color.colorBackground63));
        colors.add(new Color(R.color.colorBackground64));
        colors.add(new Color(R.color.colorBackground65));
        colors.add(new Color(R.color.colorBackground66));
        colors.add(new Color(R.color.colorBackground67));
        colors.add(new Color(R.color.colorBackground68));
        colors.add(new Color(R.color.colorBackground69));
        colors.add(new Color(R.color.colorBackground70));
        colors.add(new Color(R.color.colorBackground71));
        colors.add(new Color(R.color.colorBackground72));
        colors.add(new Color(R.color.colorBackground73));
        colors.add(new Color(R.color.colorBackground74));
        colors.add(new Color(R.color.colorBackground75));
        colors.add(new Color(R.color.colorBackground76));
        colors.add(new Color(R.color.colorBackground77));
        colors.add(new Color(R.color.colorBackground78));
        colors.add(new Color(R.color.colorBackground79));
        colors.add(new Color(R.color.colorBackground80));
        colors.add(new Color(R.color.colorBackground81));
        colors.add(new Color(R.color.colorBackground82));
        colors.add(new Color(R.color.colorBackground83));
        colors.add(new Color(R.color.colorBackground84));
        colors.add(new Color(R.color.colorBackground85));
        colors.add(new Color(R.color.colorBackground86));
        colors.add(new Color(R.color.colorBackground87));
        colors.add(new Color(R.color.colorBackground88));
        colors.add(new Color(R.color.colorBackground89));
        colors.add(new Color(R.color.colorBackground90));
        colors.add(new Color(R.color.colorBackground91));
        colors.add(new Color(R.color.colorBackground92));
        colors.add(new Color(R.color.colorBackground93));
        colors.add(new Color(R.color.colorBackground94));
        colors.add(new Color(R.color.colorBackground95));
        colors.add(new Color(R.color.colorBackground96));
        colors.add(new Color(R.color.colorBackground97));
        colors.add(new Color(R.color.colorBackground98));
        return colors;
    }


    public static List<StickerItem> getMenuSticker() {
        List<StickerItem> stickers = new ArrayList<>();
//        stickers.add(new StickerItem(R.drawable.ava_1));
//        StickerItem item = new StickerItem(R.drawable.ic_sm_1);
//        item.setNewSticker(true);
//        stickers.add(item);

        return stickers;
    }

    //get background

    public static ArrayList<ImageTemplate> getNatureList() {
        ArrayList<ImageTemplate> imageTemplates = new ArrayList<>();
        //add new background
        imageTemplates.add(new ImageTemplate(R.drawable.background_1,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_2,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_3,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_4));
        imageTemplates.add(new ImageTemplate(R.drawable.background_6,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_7,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_8,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_11));
        imageTemplates.add(new ImageTemplate(R.drawable.background_10_min));
        imageTemplates.add(new ImageTemplate(R.drawable.background_12,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_13,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_15));
        imageTemplates.add(new ImageTemplate(R.drawable.background_16));
        imageTemplates.add(new ImageTemplate(R.drawable.background_17));
        imageTemplates.add(new ImageTemplate(R.drawable.background_19));
        imageTemplates.add(new ImageTemplate(R.drawable.background_20));
        imageTemplates.add(new ImageTemplate(R.drawable.background_22,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_23));
        imageTemplates.add(new ImageTemplate(R.drawable.background_24));
        imageTemplates.add(new ImageTemplate(R.drawable.background_25));
        imageTemplates.add(new ImageTemplate(R.drawable.background_26,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_28));
        imageTemplates.add(new ImageTemplate(R.drawable.background_29));
        imageTemplates.add(new ImageTemplate(R.drawable.background_30));
        imageTemplates.add(new ImageTemplate(R.drawable.background_31));
        imageTemplates.add(new ImageTemplate(R.drawable.background_32,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_35));
        imageTemplates.add(new ImageTemplate(R.drawable.background_36,true));
        imageTemplates.add(new ImageTemplate(R.drawable.background_37));
        imageTemplates.add(new ImageTemplate(R.drawable.background_39));
        imageTemplates.add(new ImageTemplate(R.drawable.background_41));


        imageTemplates.add(new ImageTemplate(R.drawable.nature_7));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_8));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_9));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_1));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_2));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_3,true));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_4));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_5));
        imageTemplates.add(new ImageTemplate(R.drawable.nature_6));
        return imageTemplates;
    }

    public static ArrayList<ImageTemplate> getMacroList() {
        ArrayList<ImageTemplate> imageTemplates = new ArrayList<>();
        imageTemplates.add(new ImageTemplate(R.drawable.blur1));
        imageTemplates.add(new ImageTemplate(R.drawable.blur2));
        imageTemplates.add(new ImageTemplate(R.drawable.blur3,true));
        imageTemplates.add(new ImageTemplate(R.drawable.macro_1,true));
        imageTemplates.add(new ImageTemplate(R.drawable.macro_2));
        imageTemplates.add(new ImageTemplate(R.drawable.macro_3));
        imageTemplates.add(new ImageTemplate(R.drawable.macro_4,true));
        imageTemplates.add(new ImageTemplate(R.drawable.macro_5));
        imageTemplates.add(new ImageTemplate(R.drawable.macro_6));
        return imageTemplates;
    }

    public static ArrayList<ImageTemplate> getLoveList() {
        ArrayList<ImageTemplate> imageTemplates = new ArrayList<>();
        imageTemplates.add(new ImageTemplate(R.drawable.tamtrang1,true));
        imageTemplates.add(new ImageTemplate(R.drawable.tamtrang2,true));
        imageTemplates.add(new ImageTemplate(R.drawable.tamtrang3));
        imageTemplates.add(new ImageTemplate(R.drawable.template_new7));
        imageTemplates.add(new ImageTemplate(R.drawable.template_new8));
        imageTemplates.add(new ImageTemplate(R.drawable.template_new9));
        imageTemplates.add(new ImageTemplate(R.drawable.template8));
        imageTemplates.add(new ImageTemplate(R.drawable.template9));
        imageTemplates.add(new ImageTemplate(R.drawable.template10));
        return imageTemplates;
    }

    public static ArrayList<ImageTemplate> getLightList() {
        ArrayList<ImageTemplate> imageTemplates = new ArrayList<>();
        imageTemplates.add(new ImageTemplate(R.drawable.a1));
        imageTemplates.add(new ImageTemplate(R.drawable.a2,true));
        imageTemplates.add(new ImageTemplate(R.drawable.a2));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_4_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_5_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_6_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_7_m,true));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_8_m,true));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_9_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_10_m,true));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_11_m,true));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_12_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_13_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_14_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_15_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_16_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_17_m,true));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_20_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_21_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_22_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_23_m,true));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_25_m));
        imageTemplates.add(new ImageTemplate(R.drawable.bg_26_m));

        return imageTemplates;
    }

    public static ArrayList<ImageTemplate> getLifeStyleList() {
        ArrayList<ImageTemplate> imageTemplates = new ArrayList<>();
        imageTemplates.add(new ImageTemplate(R.drawable.style1,true));
        imageTemplates.add(new ImageTemplate(R.drawable.style2));
        imageTemplates.add(new ImageTemplate(R.drawable.style3));
        imageTemplates.add(new ImageTemplate(R.drawable.life_style_1,true));
        imageTemplates.add(new ImageTemplate(R.drawable.life_style_2));
        imageTemplates.add(new ImageTemplate(R.drawable.life_style_3,true));
        imageTemplates.add(new ImageTemplate(R.drawable.life_style_4));
        imageTemplates.add(new ImageTemplate(R.drawable.life_style_5));
        imageTemplates.add(new ImageTemplate(R.drawable.life_style_6));
        return imageTemplates;
    }

}
