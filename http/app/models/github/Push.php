<?php namespace github;

use Eloquent;

class Push extends Eloquent {
  protected $table = 'github_pushes';
  
  public function commits() {
    return $this->hasMany('github\Commit');
  }
}