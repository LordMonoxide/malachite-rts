<?php

class ForumCategory extends Eloquent {
  public function forums() {
    return $this->hasMany('ForumForum', 'category_id');
  }
  
  public function getNameForUriAttribute() {
    $name = strtolower($this->attributes['name']);
    $name = str_replace(' ', '-', $name);
    
    if(strlen($name) > 20) {
      $name = substr($name, 0, 20);
    }
    
    return $name;
  }
  
  public function getPathAttribute() {
    return $this->id . '-' . $this->getNameForUriAttribute();
  }
}