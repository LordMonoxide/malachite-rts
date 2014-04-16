<?php

class Post extends Eloquent {
  public function topic() {
    return $this->belongsTo('Topic');
  }
  
  public function author() {
    return $this->belongsTo('User', 'author_id');
  }
  
  public function reps() {
    return $this->hasMany('UserPostRep');
  }
  
  public function scopeNewest($query) {
    return $query->orderBy('created_at', 'DESC');
  }
}