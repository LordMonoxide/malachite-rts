package malachite.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import malachite.Game.GameInterface;
import malachite.Game.GameProxy;
import malachite.api.Lang;
import malachite.buildings.Building;
import malachite.buildings.Buildings;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.control.Button;
import malachite.engine.gfx.gui.control.Frame;
import malachite.engine.gfx.gui.control.Image;
import malachite.engine.gfx.gui.control.Label;
import malachite.pathfinding.Point;
import malachite.units.Unit;
import malachite.world.BuildingEntity;
import malachite.world.Entity;
import malachite.world.UnitEntity;
import malachite.world.World;

public class Game extends AbstractGUI implements GameInterface {
  private GameProxy _proxy;
  private PauseMenu _pause;
  
  private Font _font = FontBuilder.getInstance().getDefault();
  
  World _world;
  float _viewX, _viewY;
  int _viewW, _viewH;
  
  private Frame    _fraGame;
  private Frame    _fraPanel;
  private Frame    _fraBuildingsMenu;
  private Label    _lblBuildingsMenuTitle;
  private Button[] _btnBuildingsMenuBuilding;
  
  private ArrayList<EntityRenderer> _entities = new ArrayList<>();
  private PseudoRenderer _pseudo;
  
  private Entity[] _selectedEntities;
  
  public Game(GameProxy proxy, World world) {
    _proxy = proxy;
    _world = world;
    ready();
  }
  
  @Override
  protected void load() {
    _context.setBackColour(0, 0, 0, 1);
    
    _pause = new PauseMenu(_proxy);
    
    _fraGame = new Frame();
    _fraGame.events().addDrawHandler (new GameDrawHandler ());
    _fraGame.events().addMouseHandler(new GameMouseHandler());
    
    _fraPanel = new Frame();
    _fraPanel.setH(200);
    _fraPanel.hide();
    
    _fraBuildingsMenu = new Frame();
    _fraBuildingsMenu.setH(_fraPanel.getH());
    _fraBuildingsMenu.hide();
    
    _lblBuildingsMenuTitle = new Label();
    _lblBuildingsMenuTitle.setAutoSize(true);
    _lblBuildingsMenuTitle.setText(Lang.Game.get(Lang.GameKeys.MENU_BUILDINGS_TITLE));
    
    _btnBuildingsMenuBuilding = new Button[Buildings.count()];
    
    int i = 0;
    Buildings.each(building -> {
      _btnBuildingsMenuBuilding[i] = new Button();
      _btnBuildingsMenuBuilding[i].setText(Lang.Game.get(building.name()));
      _btnBuildingsMenuBuilding[i].setWH(48, 48);
      _btnBuildingsMenuBuilding[i].setXY(i * 56 + 8, _lblBuildingsMenuTitle.getY() + _lblBuildingsMenuTitle.getH() + 8);
      _fraBuildingsMenu.controls().add(_btnBuildingsMenuBuilding[i]);
      _btnBuildingsMenuBuilding[i].events().addClickHandler(new ControlEvents.Click() {
        @Override public void clickDbl() { }
        @Override public void click() {
          ArrayList<UnitEntity> units = new ArrayList<>();
          for(Entity e : _selectedEntities) {
            if(e instanceof UnitEntity) {
              units.add((UnitEntity)e);
            }
          }
          
          clearSelection();
          hidePanel();
          
          _pseudo = new PseudoRenderer((BuildingEntity)building.createEntity(), units.toArray(new UnitEntity[0]));
          _fraGame.controls().add(_pseudo);
        }
      });
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
    
    if(x < 64) { _viewX -= 3; }
    if(y < 64) { _viewY -= 3; }
    if(x > _context.getW() - 64) { _viewX += 3; }
    if(y > _context.getH() - 64) { _viewY += 3; }
    
    _proxy.logic();
    
    return false;
  }
  
  @Override
  public void addEntity(Entity entity) {
    EntityRenderer i = new EntityRenderer(entity);
    _fraGame.controls().add(i);
    _entities.add(i);
  }
  
  public void clickEntity(Entity entity, EntityRenderer renderer) {
    selectEntities(entity);
    
    if(entity.source instanceof Building) {
      showBuildingPanel((Building)entity.source);
    }
    
    if(entity.source instanceof Unit) {
      showUnitPanel((Unit)entity.source);
    }
  }
  
  public void showBuildingPanel(Building building) {
    _fraPanel.show();
    resize();
  }
  
  public void showUnitPanel(Unit unit) {
    _fraPanel.show();
    _fraBuildingsMenu.show();
    
    resize();
  }
  
  public void hidePanel() {
    _fraPanel.hide();
    _fraBuildingsMenu.hide();
    resize();
  }
  
  public void selectEntities(Entity... entities) {
    _selectedEntities = entities;
  }
  
  public void clearSelection() {
    _selectedEntities = null;
  }
  
  @Override
  protected boolean handleKeyUp(int key)  {
    switch(key) {
      case Keyboard.KEY_ESCAPE:
        _pause.push();
        return true;
    }
    
    return false;
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
  
  private class GameMouseHandler extends ControlEvents.Mouse {
    @Override public void move(int x, int y, int button) {
      
    }
    
    @Override public void down(int x, int y, int button) {
      
    }
    
    @Override public void up(int x, int y, int button) {
      if(_pseudo == null) {
        switch(button) {
          case 0:
            hidePanel();
            clearSelection();
            break;
            
          case 1:
            if(_selectedEntities != null) {
              for(Entity e : _selectedEntities) {
                if(e.source instanceof Unit) {
                  _proxy.moveEntity(e, new Point(x + _viewX, y + _viewY));
                }
              }
            }
            
            break;
        }
      } else {
        _fraGame.controls().remove(_pseudo);
        _proxy.placeFoundation((Building)_pseudo.entity.source, x + _viewX, y + _viewY, _pseudo.builders);
        _pseudo = null;
      }
    }
  }
  
  private class EntityRenderer extends Image {
    protected final Entity entity;
    
    private EntityRenderer(Entity entity) {
      super(
        InitFlags.WITH_BACKGROUND,
        InitFlags.WITH_DEFAULT_EVENTS,
        InitFlags.REGISTER
      );
      
      System.out.println(entity);
      this.entity = entity;
      setWH(entity.getW(), entity.getH());
      setBackgroundColour(1, 0, 1, 1);
      
      EntityRenderer t = this;
      events().addMouseHandler(new ControlEvents.Mouse() {
        @Override public void move(int x, int y, int button) { }
        @Override public void down(int x, int y, int button) { }
        @Override public void up(int x, int y, int button) {
          if(button == 0) {
            clickEntity(entity, t);
          }
        }
      });
    }
    
    @Override public void draw() {
      if(entity instanceof BuildingEntity) {
        setBackgroundColour(1, 0, 1, ((BuildingEntity)entity).completion());
      }
      
      setXY((int)(entity.getX() - _viewX), (int)(entity.getY() - _viewY));
      drawBegin();
      _font.draw(0, 0, entity.name());
      drawEnd();
      drawNext();
    }
  }
  
  private class PseudoRenderer extends Image {
    private final BuildingEntity entity;
    private final UnitEntity[] builders;
    
    private PseudoRenderer(BuildingEntity entity, UnitEntity... builders) {
      super(
        InitFlags.WITH_BACKGROUND
      );
      
      this.entity = entity;
      this.builders = builders;
      setWH(entity.getW(), entity.getH());
      setBackgroundColour(1, 0, 1, 1);
    }
    
    @Override public void draw() {
      setXY(_context.getMouseX(), _context.getMouseY());
      drawBegin();
      drawEnd();
      drawNext();
    }
  }
}