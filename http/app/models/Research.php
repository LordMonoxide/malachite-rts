<?php

class Research extends Eloquent {
  protected $table = 'research';
  
  public function building() {
    return $this->belongsTo('Building');
  }
  
  public function unlocks() {
    return $this->morphMany('Requirement', 'research');
  }
  
  public function requirements() {
    return $this->morphMany('Requirement', 'unlock');
  }
}