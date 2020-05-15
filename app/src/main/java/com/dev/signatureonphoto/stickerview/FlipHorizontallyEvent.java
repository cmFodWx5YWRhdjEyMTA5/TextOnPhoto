package com.dev.signatureonphoto.stickerview;



public class FlipHorizontallyEvent extends AbstractFlipEvent {

  @Override
  @StickerView.Flip protected int getFlipDirection() {
    return StickerView.FLIP_HORIZONTALLY;
  }
}
