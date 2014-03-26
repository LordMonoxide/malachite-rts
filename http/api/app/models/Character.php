<?php

class Character extends Eloquent {
  public function user() {
    return $this->belongsTo('User');
  }
  
  public function auth() {
    return $this->belongsTo('UserIP');
  }
  
  public function race() {
    return $this->hasOne('Race');
  }
  
  public function stats() {
    return $this->hasMany('CharacterStat');
  }
}