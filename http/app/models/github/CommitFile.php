<?php namespace github;

use Eloquent;

class CommitFile extends Eloquent {
  protected $table = 'github_commit_files';
  
  public function commit() {
    return $this->belongsTo('github\Commit');
  }
}