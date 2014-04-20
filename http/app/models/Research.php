<?php

class Research extends Eloquent {
  protected $table = 'researches';
  
  public function unlocks() {
    return $this->morphMany('Requirement', 'requirement');
  }
  
  public function requirements() {
    return $this->morphMany('Requirement', 'unlock');
  }
}