package com.dev.signatureonphoto.injection.component;

import com.dev.signatureonphoto.edittext.AddQuoteActivity;
import com.dev.signatureonphoto.edittext.EditTextActivity;
import com.dev.signatureonphoto.edittext.QuoteListActivity;
import com.dev.signatureonphoto.features.color.ColorActivity;
import com.dev.signatureonphoto.features.color.ColorMainActivity;
import com.dev.signatureonphoto.features.main.MainActivity;
import com.dev.signatureonphoto.injection.PerActivity;
import com.dev.signatureonphoto.injection.module.ActivityModule;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {



    void inject(MainActivity mainActivity);

    void inject(ColorActivity colorActivity);

    void inject(EditTextActivity editTextActivity);

    void inject(ColorMainActivity colorMainActivity);

    void inject(QuoteListActivity quoteListActivity);

    void inject(AddQuoteActivity addQuoteActivity);
}
