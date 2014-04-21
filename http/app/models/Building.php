<?php

class Building extends Eloquent {
  public function units() {
    return $this->hasMany('Unit');
  }
  
  public function research() {
    return $this->hasMany('Research');
  }
  
  public function requirements() {
    return $this->morphMany('Requirement', 'unlock');
  }
}