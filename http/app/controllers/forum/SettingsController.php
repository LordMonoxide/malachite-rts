<?php namespace forum;

use BaseController;

class SettingsController extends BaseController {
  public function __construct() {
    $this->beforeFilter('auth');
  }
  
  public function settings() {
    
  }
}