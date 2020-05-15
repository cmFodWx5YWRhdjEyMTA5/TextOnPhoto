package com.dev.signatureonphoto.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import com.dev.signatureonphoto.common.injection.module.ApplicationTestModule;
import com.dev.signatureonphoto.injection.component.AppComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {
}
