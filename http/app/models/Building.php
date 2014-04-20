<?php

class Building extends Eloquent {
  public function units() {
    return $this->hasMany('Unit');
  }
  
  public function unlocks() {
    return $this->morphMany('Requirement', 'requirement');
  }
  
  public function requirements() {
    return $this->morphMany('Requirement', 'unlock');
  }
}