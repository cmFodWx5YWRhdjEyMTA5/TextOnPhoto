package com.dev.signatureonphoto.stickerview;

import android.view.MotionEvent;

import com.dev.signatureonphoto.features.main.Event;

public class AlignEvent implements StickerIconEvent {
    Event event;

    public AlignEvent(Event event) {
        this.event = event;
    }

    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent e) {
        event.onAlignClick();
    }
}
