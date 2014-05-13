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
      case 401:
        dd($response->getData());
        break;
      
      case 409:
        return Redirect::back()->withInput(Input::only('email'))->withErrors($response->getData());
        break;
      
      default:
        dd($response->getData());
        break;
    }
    
    return Redirect::intended(URL::route('home'));
  }
  
  public function logout() {
    $request = Request::create(URL::route('api.auth.logout'), 'POST');
    $response = Route::dispatch($request);
    
    if($response->getStatusCode() === 204) {
      return Redirect::home();
    } else {
      dd($response->getData());
    }
  }
}