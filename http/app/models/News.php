<?php

class News extends Eloquent {
  public function topic() {
    return $this->belongsTo('Topic');
  }
  
  public function scopeLatest($query) {
    return $query->orderBy('created_at', 'DESC');
  }
}