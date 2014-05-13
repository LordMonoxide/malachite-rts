<?php namespace github;

use Eloquent;

class Push extends Eloquent {
  protected $table = 'github_pushes';
  
  public function commits() {
    return $this->hasMany('github\Commit');
  }
  
  public function scopeNewest($query) {
    return $query->orderBy('updated_at', 'DESC');
  }
}