package com.dev.signatureonphoto.injection.module;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

import dagger.Module;
import dagger.Provides;
import com.dev.signatureonphoto.injection.ActivityContext;

@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment providesFragment() {
        return fragment;
    }

    @Provides
    Activity provideActivity() {
        return fragment.getActivity();
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return fragment.getActivity();
    }
}
