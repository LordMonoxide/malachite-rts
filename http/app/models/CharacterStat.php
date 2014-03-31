<?php

class CharacterStat extends Eloquent {
  public $timestamps = false;
  
  public function character() {
    $this->hasOne('Character');
  }
  
  public function stat() {
    $this->hasOne('Stat');
  }
}