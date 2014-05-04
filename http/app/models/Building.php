<?php

class Building extends Eloquent {
  public function research() {
    return $this->hasMany('Research');
  }
  
  public function requirements() {
    return $this->morphMany('Requirement', 'unlock');
  }
}