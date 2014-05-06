package malachite.gui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import malachite.Game.GameInterface;
import malachite.Game.GameProxy;
import malachite.api.Lang;
import malachite.buildings.Building;
import malachite.buildings.Buildings;
import malachite.engine.gfx.AbstractContext;
import malachite.engine.gfx.AbstractDrawable;
import malachite.engine.gfx.fonts.Font;
import malachite.engine.gfx.fonts.FontBuilder;
import malachite.engine.gfx.gui.AbstractGUI;
import malachite.engine.gfx.gui.ControlEvents;
import malachite.engine.gfx.gui.control.Button;
import malachite.engine.gfx.gui.control.Frame;
import malachite.engine.gfx.gui.control.Image;
import malachite.engine.gfx.gui.control.Label;
import malachite.pathfinding.Point;
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
  
  private Frame    _fraUnitsMenu;
  private Label    _lblUnitsMenuTitle;
  private Button   _btnUnitsMenuTrain;
  
  private ArrayList<EntityRenderer> _entities = new ArrayList<>();
  private PseudoRenderer _pseudo;
  
  private AbstractDrawable _selBox;
  private EntityRenderer[] _selectedEntities;
  
  public Game(GameProxy proxy, World world) {
    _proxy = proxy;
    _world = world;
    ready();
  }
  
  @Override
  protected void load() {
    _context.setBackColour(0, 0, 0, 1);
    
    _pause = new PauseMenu(_proxy);
    
    _selBox = AbstractContext.newDrawable();
    _selBox.setVisible(false);
    _selBox.setColour(0, 0, 0, 1);
    
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
    
    _fraUnitsMenu = new Frame();
    _fraUnitsMenu.setH(_fraPanel.getH());
    _fraUnitsMenu.hide();
    
    _lblUnitsMenuTitle = new Label();
    _lblUnitsMenuTitle.setAutoSize(true);
    _lblUnitsMenuTitle.setText(Lang.Game.get(Lang.GameKeys.MENU_UNITS_TITLE));
    
    _btnUnitsMenuTrain = new Button();
    _btnUnitsMenuTrain.setXYWH(8, _lblUnitsMenuTitle.getY() + _lblUnitsMenuTitle.getH() + 4, 48, 48);
    _btnUnitsMenuTrain.setText(Lang.Game.get(Lang.GameKeys.MENU_UNITS_TRAIN));
    _btnUnitsMenuTrain.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        _proxy.trainUnit((BuildingEntity)_selectedEntities[0].entity);
      }
    });
    
    int i = 0;
    Buildings.each(building -> {
      _btnBuildingsMenuBuilding[i] = new Button();
      _btnBuildingsMenuBuilding[i].setText(Lang.Game.get(building.name()));
      _btnBuildingsMenuBuilding[i].setWH(48, 48);
      _btnBuildingsMenuBuilding[i].setXY(i * 56 + 8, _lblBuildingsMenuTitle.getY() + _lblBuildingsMenuTitle.getH() + 4);
      _fraBuildingsMenu.controls().add(_btnBuildingsMenuBuilding[i]);
      _btnBuildingsMenuBuilding[i].events().addClickHandler(new ControlEvents.Click() {
        @Override public void clickDbl() { }
        @Override public void click() {
          ArrayList<UnitEntity> units = new ArrayList<>();
          for(EntityRenderer e : _selectedEntities) {
            if(e.entity instanceof UnitEntity) {
              units.add((UnitEntity)e.entity);
            }
          }
          
          clearSelection();
          hidePanel();
          
          _pseudo = new PseudoRenderer((BuildingEntity)building.createEntity(), units.toArray(new UnitEntity[0]));
          _fraGame.controls().add(_pseudo);
        }
      });
    });
    
    _fraUnitsMenu.controls().add(_btnUnitsMenuTrain);
    
    _fraBuildingsMenu.controls().add(_lblBuildingsMenuTitle);
    _fraUnitsMenu.controls().add(_lblUnitsMenuTitle);
    
    _fraPanel.controls().add(_fraBuildingsMenu);
    _fraPanel.controls().add(_fraUnitsMenu);
    
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
    _fraUnitsMenu.setW(_fraPanel.getW());
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
    selectEntities(renderer);
  }
  
  public void showBuildingPanel(BuildingEntity building) {
    _fraPanel.show();
    _fraUnitsMenu.show();
    resize();
  }
  
  public void showUnitPanel(EntityRenderer... unit) {
    _fraPanel.show();
    _fraBuildingsMenu.show();
    resize();
  }
  
  public void hidePanel() {
    _fraPanel.hide();
    _fraBuildingsMenu.hide();
    _fraUnitsMenu.hide();
    resize();
  }
  
  public void selectEntities(EntityRenderer... entities) {
    clearSelection();
    
    _selectedEntities = entities;
    
    boolean onlyUnits = true;
    boolean onlyBuildings = true;
    
    for(EntityRenderer e : entities) {
      e.setBorderColour(1, 1, 1, 1);
      
      if(e.entity instanceof UnitEntity) {
        onlyBuildings = false;
      } else if(e.entity instanceof BuildingEntity) {
        onlyUnits = false;
      }
    }
    
    if(onlyUnits) {
      showUnitPanel(entities);
    } else if(onlyBuildings) {
      if(entities.length == 1) {
        showBuildingPanel((BuildingEntity)entities[0].entity);
      }
    }
  }
  
  public void clearSelection() {
    if(_selectedEntities != null) {
      for(EntityRenderer e : _selectedEntities) {
        e.setBorderColour(0, 0, 0, 1);
      }
    }
    
    hidePanel();
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
      _selBox.draw();
      
      _matrix.pop();
    }
  }
  
  private class GameMouseHandler extends ControlEvents.Mouse {
    @Override public void move(int x, int y, int button) {
      if(_selBox.getVisible()) {
        _selBox.setWH((x + _viewX) - _selBox.getX(), (y + _viewY) - _selBox.getY());
        _selBox.createBorder();
      }
    }
    
    @Override public void down(int x, int y, int button) {
      if(button == 0) {
        _selBox.setXYWH(x + _viewX, y + _viewY, 0, 0);
        _selBox.setVisible(true);
        _selBox.createBorder();
      }
    }
    
    @Override public void up(int x, int y, int button) {
      if(_pseudo == null) {
        switch(button) {
          case 0:
            clearSelection();
            break;
            
          case 1:
            if(_selectedEntities != null) {
              for(EntityRenderer e : _selectedEntities) {
                if(e.entity instanceof UnitEntity) {
                  _proxy.moveEntity(e.entity, new Point(x + _viewX, y + _viewY));
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
      
      if(_selBox.getVisible()) {
        _selBox.setVisible(false);
        
        ArrayList<EntityRenderer> entities = new ArrayList<>();
        for(EntityRenderer e : _entities) {
          float sx = _selBox.getX() - _viewX;
          float sy = _selBox.getY() - _viewY;
          if(e.getX() >= sx && e.getX() <= sx + _selBox.getW() &&
             e.getY() >= sy && e.getY() <= sy + _selBox.getH()) {
            if(e.entity instanceof UnitEntity) {
              entities.add(e);
            }
          }
        }
        
        if(!entities.isEmpty()) {
          selectEntities(entities.toArray(new EntityRenderer[0])); //TODO
        }
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