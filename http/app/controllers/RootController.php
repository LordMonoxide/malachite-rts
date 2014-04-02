<?php

class RootController extends BaseController {
  public function __construct() {
    $this->beforeFilter('nauth', ['only' => ['login']]);
  }
  
  public function home() {
    
  }
  
  public function login() {
    return View::make('login');
  }
}