<?php

class RootController extends BaseController {
  public function __construct() {
    $this->beforeFilter('nauth', ['only' => ['login']]);
  }
  
  public function home() {
    return View::make('index');
  }
  
  public function login() {
    return View::make('login');
  }
}