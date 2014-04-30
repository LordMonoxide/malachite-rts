package malachite.gui;

import java.util.ArrayList;

import malachite.Game.GameInterface;
import malachite.Game.GameProxy;
import malachite.api.Lang;
import malachite.buildings.AbstractBuilding;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.control.Button;
import malachite.engine.gfx.gui.control.Frame;
import malachite.engine.gfx.gui.control.Image;
import malachite.engine.gfx.gui.control.Label;
import malachite.units.AbstractUnit;
import malachite.units.Villager;
import malachite.world.Entity;
import malachite.world.World;

public class Game extends AbstractGUI implements GameInterface {
  private GameProxy _proxy;
  
  World _world;
  float _viewX, _viewY;
  int _viewW, _viewH;
  
  private Frame    _fraGame;
  private Frame    _fraPanel;
  private Frame    _fraBuildingsMenu;
  private Label    _lblBuildingsMenuTitle;
  private Button[] _btnBuildingsMenuBuilding;
  
  private ArrayList<Image> _entities = new ArrayList<>();
  
  public Game(GameProxy proxy, World world) {
    _proxy = proxy;
    _world = world;
  }
  
  @Override
  protected void load() {
    _context.setBackColour(0, 0, 0, 1);
    
    _fraGame = new Frame();
    _fraGame.events().addDrawHandler (new GameDrawHandler ());
    _fraGame.events().addClickHandler(new GameClickHandler());
    
    _fraPanel = new Frame();
    _fraPanel.setH(200);
    _fraPanel.hide();
    
    _fraBuildingsMenu = new Frame();
    _fraBuildingsMenu.setH(_fraPanel.getH());
    _fraBuildingsMenu.hide();
    
    _lblBuildingsMenuTitle = new Label();
    _lblBuildingsMenuTitle.setAutoSize(true);
    _lblBuildingsMenuTitle.setText(Lang.Game.get(Lang.GameKeys.MENU_BUILDINGS_TITLE));
    
    _btnBuildingsMenuBuilding = new Button[_proxy.buildingCount()];
    
    int i = 0;
    _proxy.eachBuilding((building) -> {
      _btnBuildingsMenuBuilding[i] = new Button();
      _btnBuildingsMenuBuilding[i].setText(building.name);
      _btnBuildingsMenuBuilding[i].setWH(48, 48);
      _btnBuildingsMenuBuilding[i].setXY(i * 56 + 8, _lblBuildingsMenuTitle.getY() + _lblBuildingsMenuTitle.getH() + 8);
      _fraBuildingsMenu.controls().add(_btnBuildingsMenuBuilding[i]);
    });
    
    _fraBuildingsMenu.controls().add(_lblBuildingsMenuTitle);
    
    _fraPanel.controls().add(_fraBuildingsMenu);
    
    controls().add(_fraGame);
    controls().add(_fraPanel);
    
    resize();
  }
  
  @Override
  public void destroy() {
    
  }
  
  @Override
  protected void resize() {
    _viewW = (int)Math.ceil(_context.getW() / 32);
    _viewH = (int)Math.ceil(_context.getH() / 32);
    
    if(_fraPanel.isVisible()) {
      _fraGame.setWH(_context.getW(), _context.getH() - _fraPanel.getH());
    } else {
      _fraGame.setWH(_context.getW(), _context.getH());
    }
    
    _fraPanel.setW(_context.getW());
    _fraPanel.setY(_fraGame.getH());
    _fraBuildingsMenu.setW(_fraPanel.getW());
  }
  
  @Override
  protected void draw() {
    
  }
  
  @Override
  protected boolean logic() {
    int x = _context.getMouseX();
    int y = _context.getMouseY();
    
    if(x < 64) {
      _viewX -= 3;
    }
    
    if(y < 64) {
      _viewY -= 3;
    }
    
    if(x > _context.getW() - 64) {
      _viewX += 3;
    }
    
    if(y > _context.getH() - 64) {
      _viewY += 3;
    }
    
    return false;
  }
  
  @Override
  public void addEntity(Entity entity) {
    EntityRenderer i = new EntityRenderer(entity);
    _fraGame.controls().add(i);
    _entities.add(i);
  }
  
  public void clickEntity(Entity entity, EntityRenderer renderer) {
    if(entity.source instanceof AbstractBuilding) {
      showBuildingPanel((AbstractBuilding)entity.source);
    }
    
    if(entity.source instanceof AbstractUnit) {
      showUnitPanel((AbstractUnit)entity.source);
    }
  }
  
  public void showBuildingPanel(AbstractBuilding building) {
    _fraPanel.show();
    resize();
  }
  
  public void showUnitPanel(AbstractUnit unit) {
    _fraPanel.show();
    
    if(unit instanceof Villager) {
      _fraBuildingsMenu.show();
    }
    
    resize();
  }
  
  public void hidePanel() {
    _fraPanel.hide();
    _fraBuildingsMenu.hide();
    resize();
  }
  
  private class GameDrawHandler extends ControlEvents.Draw {
    @Override public void draw() {
      _matrix.push();
      _matrix.translate(-_viewX, -_viewY);
      
      int x1 = (int)Math.max(0, _viewX / 32);
      int y1 = (int)Math.max(0, _viewY / 32);
      int w1 = Math.min(x1 + _viewW + 1, _world.getW() - 1);
      int h1 = Math.min(y1 + _viewH + 1, _world.getH() - 1);
      
      _world.draw(x1, y1, w1, h1);
      
      _matrix.pop();
    }
  }
  
  private class GameClickHandler extends ControlEvents.Click {
    @Override public void clickDbl() { }
    @Override public void click() {
      hidePanel();
    }
  }
  
  private class EntityRenderer extends Image {
    private Entity _entity;
    
    private EntityRenderer(Entity entity) {
      super(
        InitFlags.WITH_BACKGROUND,
        InitFlags.WITH_DEFAULT_EVENTS,
        InitFlags.REGISTER
      );
      
      System.out.println(entity);
      _entity = entity;
      setWH(_entity.getW(), _entity.getH());
      setBackgroundColour(1, 0, 1, 1);
      
      EntityRenderer t = this;
      events().addClickHandler(new ControlEvents.Click() {
        @Override public void clickDbl() { }
        @Override public void click() {
          clickEntity(entity, t);
        }
      });
    }
    
    @Override public void draw() {
      setXY((int)(_entity.getX() - _viewX), (int)(_entity.getY() - _viewY));
      drawBegin();
      drawEnd();
      drawNext();
    }
  }
}