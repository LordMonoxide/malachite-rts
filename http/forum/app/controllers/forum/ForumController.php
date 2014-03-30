<?php namespace forum;

use View;

use BaseController;

class ForumController extends BaseController {
  public function index() {
    
  }
  
  public function login() {
    return View::make('login');
  }
}