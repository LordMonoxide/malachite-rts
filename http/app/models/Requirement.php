<?php

class Requirement extends Eloquent {
  public function unlock() {
    return $this->morphTo();
  }
  
  public function research() {
    return $this->belongsTo('Research');
  }
}