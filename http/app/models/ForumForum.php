<?php

class ForumForum extends Eloquent {
  public function category() {
    return $this->belongsTo('ForumCategory', 'category_id');
  }
  
  public function parent() {
    return $this->belongsTo('ForumForum', 'forum_id');
  }
  
  public function children() {
    return $this->hasMany('ForumForum', 'parent_id');
  }
  
  public function posts() {
    return $this->hasMany('ForumPost', 'forum_id');
  }
}