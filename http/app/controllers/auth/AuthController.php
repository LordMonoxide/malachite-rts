<?php namespace auth;

use Input;
use Redirect;
use Request;
use Route;
use URL;

use BaseController;

class AuthController extends BaseController {
  public function login() {
    $request = Request::create(URL::route('api.auth.login'), 'POST', Input::only('email', 'password'));
    $response = Route::dispatch($request);
    
    switch($response->getStatusCode()) {
      case 200:
        
        break;
    }
    
    return Redirect::intended('home');
  }
}