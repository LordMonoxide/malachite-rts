<?php

class News extends Eloquent {
  public function post() {
    return $this->belongsTo('Post');
  }
  
  public function scopeLatest($query) {
    return $query->orderBy('created_at', 'DESC');
  }
}