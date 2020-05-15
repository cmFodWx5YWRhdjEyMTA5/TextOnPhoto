package com.dev.signatureonphoto.injection.component;

import com.dev.signatureonphoto.features.home.HomeFragment;
import com.dev.signatureonphoto.injection.PerFragment;
import com.dev.signatureonphoto.injection.module.FragmentModule;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(HomeFragment homeFragment);

}
