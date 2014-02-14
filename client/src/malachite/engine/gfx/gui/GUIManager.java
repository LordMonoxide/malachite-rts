package malachite.engine.gfx.gui;

import java.util.concurrent.ConcurrentLinkedDeque;

public class GUIManager {
  protected ConcurrentLinkedDeque<AbstractGUI> _gui = new ConcurrentLinkedDeque<AbstractGUI>();

  public void push(AbstractGUI gui) {
    _gui.push(gui);
  }

  public void pop() {
    _gui.pop();
  }

  public void pop(AbstractGUI gui) {
    _gui.remove(gui);
  }

  public void clear() {
    _gui.clear();
  }

  public void destroy() {
    for(AbstractGUI gui : _gui) {
      gui.destroy();
    }

    clear();
  }

  public void draw() {
    AbstractGUI[] g = new AbstractGUI[_gui.size()];
    g = _gui.toArray(g);

    for(int i = _gui.size(); --i >= 0;) {
      if(g[i]._loaded) {
        g[i].drawGUI();
      }
    }
  }

  public void logic() {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.logicGUI()) {
          break;
        }
      }
    }
  }

  public void resize() {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        gui.resize();
      }
    }
  }

  public void mouseMove(int x, int y) {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.mouseMove(x, y)) {
          break;
        }
      }
    }
  }

  public void mouseDown(int x, int y, int button) {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.mouseDown(x, y, button)) {
          break;
        }
      }
    }
  }

  public void mouseUp(int x, int y, int button) {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.mouseUp(x, y, button)) {
          break;
        }
      }
    }
  }

  public void mouseWheel(int delta) {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.mouseWheel(delta)) {
          break;
        }
      }
    }
  }

  public void keyDown(int key) {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.keyDown(key)) {
          break;
        }
      }
    }
  }

  public void keyUp(int key) {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.keyUp(key)) {
          break;
        }
      }
    }
  }

  public void charDown(char c) {
    for(AbstractGUI gui : _gui) {
      if(gui._loaded) {
        if(gui.charDown(c)) {
          break;
        }
      }
    }
  }
}
