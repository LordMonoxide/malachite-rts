<?php

class Unit extends Eloquent {
  public function building() {
    return $this->belongsTo('Building');
  }
  
  public function requirements() {
    return $this->morphMany('Requirement', 'unlock');
  }
}