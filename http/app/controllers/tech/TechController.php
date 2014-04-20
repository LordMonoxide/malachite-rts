<?php namespace tech;

use Request;
use Route;
use URL;
use View;

use BaseController;

class TechController extends BaseController {
  public function __construct() {
    $this->beforeFilter('user.security');
  }
  
  public function all() {
    $request = Request::create(URL::route('api.storage.tech.buildings.all'));
    $response = Route::dispatch($request);
    
    switch($response->getStatusCode()) {
      case 200:
        $buildings = $response->getData();
        break;
        
      default:
        dd($response);
        break;
    }
    
    $request = Request::create(URL::route('api.storage.tech.research.all'));
    $response = Route::dispatch($request);
    
    switch($response->getStatusCode()) {
      case 200:
        $research = $response->getData();
        break;
        
      default:
        dd($response);
        break;
    }
    
    return View::make('tech.all')->with('buildings', $buildings)->with('research', $research);
  }
}