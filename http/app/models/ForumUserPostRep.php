<?php

class ForumUserPostRep extends Eloquent {
  public function user() {
    return $this->belongsTo('User');
  }
  
  public function post() {
    return $this->belongsTo('ForumPost', 'post_id');
  }
  
  public function scopeMine($query) {
    return $query->where('user_id', '=', Auth::user()->id);
  }
}