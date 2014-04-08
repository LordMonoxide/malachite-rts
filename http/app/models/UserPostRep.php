<?php

class UserPostRep extends Eloquent {
  public function user() {
    return $this->belongsTo('User');
  }
  
  public function post() {
    return $this->belongsTo('Post');
  }
  
  public function scopeMine($query) {
    return $query->where('user_id', '=', Auth::user()->id);
  }
}