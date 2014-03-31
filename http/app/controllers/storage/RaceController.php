<?php namespace storage;

use Controller;
use Response;

use Race;

class RaceController extends Controller {
  public function __construct() {
    $this->beforeFilter('user.security');
  }
  
  public function all() {
    return Response::json(Race::all(), 200);
  }
}