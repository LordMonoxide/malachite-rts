<?php

class ForumPost extends Eloquent {
  public function topic() {
    return $this->belongsTo('ForumTopic', 'topic_id');
  }
  
  public function author() {
    return $this->belongsTo('User', 'author_id');
  }
}