package com.dev.signatureonphoto.features.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;

import com.dev.signatureonphoto.injection.component.ActivityComponent;
import timber.log.Timber;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private long activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        activityId =
                savedInstanceState != null
                        ? savedInstanceState.getLong(KEY_ACTIVITY_ID)
                        : NEXT_ID.getAndIncrement();

        attachView();
    }

    protected abstract int getLayout();

    protected abstract void inject(ActivityComponent activityComponent);

    protected abstract void attachView();

    protected abstract void detachPresenter();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, activityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", activityId);
//            componentsArray.remove(activityId);
        }
        detachPresenter();
        super.onDestroy();
    }
}
