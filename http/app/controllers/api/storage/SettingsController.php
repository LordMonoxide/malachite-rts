<?php namespace api\storage;

use BaseController;
use GameSetting;

class SettingsController extends BaseController {
  public function all() {
    return GameSetting::with('buildings')->with('units')->get();
  }
}