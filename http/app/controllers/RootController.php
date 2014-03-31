<?php

class RootController extends BaseController {
  public function home() {
    
  }
  
  public function login() {
    return View::make('login');
  }
}