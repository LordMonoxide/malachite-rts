<?php

class ForumPost extends Eloquent {
  public function forum() {
    return $this->belongsTo('ForumForum', 'forum_id');
  }
  
  public function creator() {
    return $this->belongsTo('User', 'user_id');
  }
}