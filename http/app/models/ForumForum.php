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
  
  public function topics() {
    return $this->hasMany('ForumTopic', 'forum_id');
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
    if($this->attributes['parent_id'] != null) {
      $path = $this->id . '-' . $this->getNameForUriAttribute();
      $parent = ForumForum::where('id', '=', $this->attributes['parent_id'])->first();
      while($parent != null) {
        $path = $parent->id . '-' . $parent->getNameForUriAttribute() . '/' . $path;
        $parent = $parent->parent;
      }
    } else {
      $path = $this->id . '-' . $this->getNameForUriAttribute();
    }
    
    return $path;
  }
}