package com.dev.signatureonphoto.features.color;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dev.signatureonphoto.R;
import com.dev.signatureonphoto.features.base.BaseActivity;
import com.dev.signatureonphoto.injection.component.ActivityComponent;
import com.dev.signatureonphoto.util.Constants;

public class ColorMainActivity extends BaseActivity {
    private ImageView btnBack;
    private RecyclerView rcvColor;
    private ColorBackgroundAdapter colorAdapter;
    private LinearLayout btnNext;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnBack = findViewById(R.id.btnBack);
        rcvColor = findViewById(R.id.rcvColor);
        btnNext=findViewById(R.id.layout_next);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        colorAdapter = new ColorBackgroundAdapter(Constants.getColorBackgrounds(), ColorMainActivity.this, new ColorBackgroundAdapter.OnItemClickListener() {
            @Override
            public void onItemCheck(View view, int position) {
                btnNext.setVisibility(View.VISIBLE);
                count=position;

            }
        });
        rcvColor.setAdapter(colorAdapter);
        rcvColor.setHasFixedSize(true);
        rcvColor.setLayoutManager(new GridLayoutManager(ColorMainActivity.this, 3));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendColor(Constants.COLOR_RESULT);

            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_color;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {

    }

    @Override
    protected void detachPresenter() {

    }

    private void sendColor(int colorResult) {
        Intent intent=getIntent();
        intent.putExtra("color", count);
        setResult(colorResult, intent);
        finish();
    }

}
