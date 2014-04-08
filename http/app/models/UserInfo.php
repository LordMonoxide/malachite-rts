<?php

class UserInfo extends Eloquent {
  public $primaryKey = 'user_id';
  public $timestamps = false;
  
  public function user() {
    return $this->belongsTo('User');
  }
  
  public function getRepAttribute() {
    $rep = $this->rep_pos - $this->rep_neg;
    if($rep > 0) { $rep = '+' . $rep; }
    return $rep;
  }
}