<?php namespace tech;

use Request;
use Route;
use URL;
use View;

use BaseController;
use Building;
use Research;
use Unit;

class TechController extends BaseController {
  public function __construct() {
    $this->beforeFilter('auth');
  }
  
  public function all() {
    $buildings = Building::with('requirements')->with('units')->get();
    $research  = Research::with('unlocks')->with('requirements')->get();
    $units     = Unit::with('requirements')->get();
    return View::make('tech.all')->with('buildings', $buildings)->with('research', $research)->with('units', $units);
  }
  
  public function buildings() {
    $buildings = Building::with('requirements')->with('units')->get();
    return View::make('tech.buildings.all')->with('buildings', $buildings);
  }
  
  public function building($building) {
    return View::make('tech.single')->with('type', 'buildings')->with('data', $building);
  }
  
  public function researches() {
    $research = Research::with('unlocks')->with('requirements')->get();
    return View::make('tech.research.all')->with('research', $research);
  }
  
  public function research($research) {
    return View::make('tech.single')->with('type', 'research')->with('data', $research);
  }
  
  public function units() {
    $units = Unit::with('requirements')->get();
    return View::make('tech.units.all')->with('units', $units);
  }
  
  public function unit($unit) {
    return View::make('tech.single')->with('type', 'units')->with('data', $unit);
  }
}