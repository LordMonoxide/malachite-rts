<?php namespace forum;

use View;

use BaseController;

class SettingsController extends BaseController {
  public function __construct() {
    $this->beforeFilter('auth');
  }
  
  public function settings() {
    return View::make('forum.settings');
  }
}