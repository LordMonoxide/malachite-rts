<?php

class ForumForum extends Eloquent {
  public function parent() {
    return $this->belongsTo('ForumForum', 'parent_id');
  }
  
  public function children() {
    return $this->hasMany('ForumForum', 'parent_id');
  }
  
  public function topics() {
    return $this->hasMany('ForumTopic', 'forum_id');
  }
  
  public function scopeRoot($query) {
    return $query->whereNull('parent_id');
  }
}