package io.github.singlerr.sgadminui.client.ui;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class StrokeDrawable extends Drawable {

  private final Supplier<Integer> strokeWidthSupplier;
  private final Paint paint = Paint.obtain();

  @Override
  public void draw(Canvas canvas) {
    paint.setStroke(true);
    paint.setStrokeWidth(strokeWidthSupplier.get());

    canvas.drawRect(getBounds(), paint);
    paint.recycle();
  }
}
