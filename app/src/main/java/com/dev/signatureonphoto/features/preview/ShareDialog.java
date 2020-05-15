package com.dev.signatureonphoto.features.preview;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;

import com.dev.signatureonphoto.R;

public class ShareDialog extends BottomSheetDialog implements View.OnClickListener {

    public static final int SHARE_FACEBOOK = 0;
    public static final int SHARE_INSTAGRAMS = 1;
    public static final int SHARE_MESSENGER = 2;
    public static final int SHARE_TWITER = 3;
    public static final int SHARE_WHAT_APP = 4;
    private CallbackDialog callbackDialog;
    private ImageView imgFacebook;
    private ImageView imgTwitter;
    private ImageView imgInstagram;
    private ImageView imgMessenger;
    private ImageView imgWhatApp;

    public ShareDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.share_dialog);
        initViews();
    }

    public void setCallbackDialog(CallbackDialog callbackDialog) {
        this.callbackDialog = callbackDialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_fb:
                callbackDialog.onClick(SHARE_FACEBOOK);
                break;
            case R.id.img_insta:
                callbackDialog.onClick(SHARE_INSTAGRAMS);
                break;
            case R.id.img_messenger:
                callbackDialog.onClick(SHARE_MESSENGER);
                break;
            case R.id.img_twiter:
                callbackDialog.onClick(SHARE_TWITER);
                break;
            case R.id.img_what_app:
                callbackDialog.onClick(SHARE_WHAT_APP);
                break;
            default:
                callbackDialog.onClick(SHARE_FACEBOOK);

        }
    }

    public interface CallbackDialog {
        void onClick(int shareType);
    }

    private void initViews() {
        imgFacebook = findViewById(R.id.img_fb);
        imgInstagram = findViewById(R.id.img_insta);
        imgMessenger = findViewById(R.id.img_messenger);
        imgTwitter = findViewById(R.id.img_twiter);
        imgWhatApp = findViewById(R.id.img_what_app);

        imgMessenger.setOnClickListener(this);
        imgInstagram.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);
        imgTwitter.setOnClickListener(this);
        imgWhatApp.setOnClickListener(this);
    }
}
