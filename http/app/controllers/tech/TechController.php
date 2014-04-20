<?php namespace tech;

use Request;
use Route;
use URL;
use View;

use BaseController;
use Building;
use Research;

class TechController extends BaseController {
  public function __construct() {
    $this->beforeFilter('user.security');
  }
  
  public function all() {
    $buildings = Building::with('unlocks')->with('requirements')->get();
    $research  = Research::with('unlocks')->with('requirements')->get();
    return View::make('tech.all')->with('buildings', $buildings)->with('research', $research);
  }
}