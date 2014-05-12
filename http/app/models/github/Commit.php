<?php namespace github;

use Eloquent;

class Commit extends Eloquent {
  protected $table = 'github_commits';
  
  public function push() {
    return $this->belongsTo('github\Push');
  }
  
  public function files() {
    return $this->hasMany('github\CommitFile');
  }
}