<?php

class UserSecurityQuestion extends Eloquent {
  public function user() {
    return $this->belongsTo('User');
  }
}