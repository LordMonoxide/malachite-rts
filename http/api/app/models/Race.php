<?php

class Race extends Eloquent {
  public $timestamps = false;
  
  public function characters() {
    return $this->hasMany('Character');
  }
}