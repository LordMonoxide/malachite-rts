<?php namespace api\storage;

use Controller;
use Response;

use Building;
use Research;
use Unit;

class TechController extends Controller {
  public function __construct() {
    $this->beforeFilter('user.security');
  }
  
  public function buildings() {
    return Response::json(Building::all(), 200);
  }
  
  public function research() {
    return Response::json(Research::all(), 200);
  }
  
  public function units() {
    return Response::json(Unit::all(), 200);
  }
}