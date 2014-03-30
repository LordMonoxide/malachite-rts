<?php namespace forum;

use BaseController;

class AuthController extends BaseController {
  public function login() {
    $request  = Request::create('api.malachite.monoxidedesign.com/login', 'POST', Input::all());
    $response = Route::dispatch($request);
    
    if($response->getStatusCode() !== 200) {
      return $response->getContent();
    }
    
    dd($response->getData());
  }
}