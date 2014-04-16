<?php

class Topic extends Eloquent {
  public function forum() {
    return $this->belongsTo('Forum');
  }
  
  public function creator() {
    return $this->belongsTo('User', 'creator_id');
  }
  
  public function posts() {
    return $this->hasMany('Post');
  }
  
  public function news() {
    return $this->hasOne('News');
  }
  
  public function scopeNewest($query) {
    return $query->orderBy('updated_at', 'DESC');
  }
  
  public function getNameForUriAttribute() {
    $name = strtolower($this->title);
    $name = str_replace(' ', '-', $name);
    
    if(strlen($name) > 20) {
      $name = substr($name, 0, 20);
    }
    
    return $name . '-';
  }
}