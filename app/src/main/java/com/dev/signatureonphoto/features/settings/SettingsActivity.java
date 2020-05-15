package com.dev.signatureonphoto.features.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dev.signatureonphoto.Constants;
import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.features.purchased.PurchasedActivity;
import com.dev.signatureonphoto.features.saved.SavedActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dev.signatureonphoto.Constants.POLICY_LINK;
public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.btn_close)
    ImageView btnClose;
    @BindView(R.id.layout_vip_pro)
    RelativeLayout layoutVipPro;
    @BindView(R.id.ln_check_update)
    LinearLayout lnCheckUpdate;
    @BindView(R.id.ln_pro_version)
    LinearLayout lnProVersion;
    @BindView(R.id.ln_report_bug)
    LinearLayout lnReportBug;
    @BindView(R.id.ln_policy)
    LinearLayout lnPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_close, R.id.layout_vip_pro, R.id.ln_check_update, R.id.ln_pro_version, R.id.ln_report_bug, R.id.ln_policy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.layout_vip_pro:
                startActivity(new Intent(SettingsActivity.this, PurchasedActivity.class));
                break;
            case R.id.ln_check_update:
                Intent intentRate = new Intent(Intent.ACTION_VIEW);
                intentRate.setData(Uri.parse(Constants.PLAY_STORE_LINK + getPackageName()));
                startActivity(intentRate);
                break;
            case R.id.ln_pro_version:
                startActivity(new Intent(SettingsActivity.this, PurchasedActivity.class));
                break;
            case R.id.ln_report_bug:
                String mailSubject = getString(R.string.mail_subject);
                String mailContent = getString(R.string.mail_content);
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType(Constants.MAIL_TYPE);
                mailIntent.putExtra(Intent.EXTRA_EMAIL, Constants.MAIL_LIST);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                mailIntent.putExtra(Intent.EXTRA_TEXT, mailContent);
                startActivity(Intent.createChooser(mailIntent, mailSubject + ":"));
                break;
            case R.id.ln_policy:
                Intent intentPolicy = new Intent(Intent.ACTION_VIEW);
                intentPolicy.setData(Uri.parse(POLICY_LINK));
                startActivity(intentPolicy);
                break;
        }
    }
}
