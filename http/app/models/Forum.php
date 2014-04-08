<?php

class Forum extends Eloquent {
  public function parent() {
    return $this->belongsTo('Forum', 'parent_id');
  }
  
  public function children() {
    return $this->hasMany('Forum', 'parent_id');
  }
  
  public function topics() {
    return $this->hasMany('Topic');
  }
  
  public function scopeRoot($query) {
    return $query->whereNull('parent_id');
  }
}