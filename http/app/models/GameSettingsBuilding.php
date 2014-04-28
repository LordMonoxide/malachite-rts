<?php

class GameSettingsBuilding extends Eloquent {
  public function settings() {
    return $this->belongsTo('GameSetting');
  }
}