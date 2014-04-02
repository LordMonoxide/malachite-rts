<?php

class ForumCategory extends Eloquent {
  public function forums() {
    return $this->hasMany('ForumForum', 'category_id');
  }
}