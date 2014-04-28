<?php

class GameSettingsUnit extends Eloquent {
  public function settings() {
    return $this->belongsTo('GameSetting');
  }
}