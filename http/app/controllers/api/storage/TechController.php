<?php namespace api\storage;

use Controller;
use Response;

use Building;
use Research;

class TechController extends Controller {
  public function __construct() {
    $this->beforeFilter('user.security');
  }
  
  public function buildings() {
    return Response::json(Building::with('unlocks')->with('requirements')->get(), 200);
  }
  
  public function research() {
    return Response::json(Research::with('unlocks')->with('requirements')->get(), 200);
  }
}