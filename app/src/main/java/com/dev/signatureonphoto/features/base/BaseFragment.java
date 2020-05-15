package com.dev.signatureonphoto.features.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;
import com.dev.signatureonphoto.injection.component.FragmentComponent;

import timber.log.Timber;

public abstract class BaseFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private long fragmentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentId =
                savedInstanceState != null
                        ? savedInstanceState.getLong(KEY_FRAGMENT_ID)
                        : NEXT_ID.getAndIncrement();
        attachView();
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getLayout();

    protected abstract void inject(FragmentComponent fragmentComponent);

    protected abstract void attachView();

    protected abstract void detachPresenter();

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, fragmentId);
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", fragmentId);
//            componentsArray.remove(fragmentId);
        }
        detachPresenter();
        super.onDestroy();
    }
}
