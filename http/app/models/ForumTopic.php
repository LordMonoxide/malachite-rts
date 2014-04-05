<?php

class ForumTopic extends Eloquent {
  public function forum() {
    return $this->belongsTo('ForumForum', 'forum_id');
  }
  
  public function creator() {
    return $this->belongsTo('User', 'creator_id');
  }
  
  public function posts() {
    return $this->hasMany('ForumPost', 'topic_id');
  }
  
  public function getNameForUriAttribute() {
    $name = strtolower($this->title);
    $name = str_replace(' ', '-', $name);
    
    if(strlen($name) > 20) {
      $name = substr($name, 0, 20);
    }
    
    return $name . '-';
  }
}