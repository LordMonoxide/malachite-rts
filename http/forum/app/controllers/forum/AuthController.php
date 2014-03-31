<?php namespace forum;

use Input;
use Request;
use Response;
use Route;

use BaseController;

class AuthController extends BaseController {
  public function login() {
    //$request  = Request::create('api.malachite.monoxidedesign.com/auth/login', 'POST', Input::all());
    $request  = Request::create('http://api.malachite.monoxidedesign.com/auth/check', 'GET');
    $response = Route::dispatch($request);
    
    if($response->getStatusCode() !== 200) {
      return $response->getContent();
    }
    
    dd($response->getData());
  }
}