package malachite.engine.gfx.fonts;

import malachite.engine.gfx.AbstractMatrix;

class FontRenderState {
  Font font;
  int x, y, w, h;
  int mask;
  float[] c;
  
  AbstractMatrix matrix;

  FontRenderState(Font font, int x, int y, int w, int h, int mask, float[] c, AbstractMatrix matrix) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.mask = mask;
    this.c = c;
    this.matrix = matrix;
  }
  
  void newLine() {
    matrix.pop();
    matrix.translate(0, font.getH());
    matrix.push();
    x = 0;
  }
}