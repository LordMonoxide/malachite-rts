<?php

class GameSetting extends Eloquent {
  public function buildings() {
    return $this->hasMany('GameSettingsBuilding');
  }
  
  public function units() {
    return $this->hasMany('GameSettingsUnit');
  }
}