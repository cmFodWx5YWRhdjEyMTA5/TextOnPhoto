package com.dev.signatureonphoto.edittext;

import com.dev.signatureonphoto.database.Quote;

public interface QuoteListener {
    void onQuoteSelected(String quote);
    void addQuote();
    void onDelete();
    void onUpload(Quote quote);
    void onUploadSuccess();
    void ononUploadFailed();
}
