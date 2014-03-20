<?php

use Illuminate\Auth\UserInterface;
use Illuminate\Auth\Reminders\RemindableInterface;

class User extends Eloquent implements UserInterface, RemindableInterface {
protected $hidden = ['password'];
  
  public function getAuthIdentifier() {
    return $this->getKey();
  }
  
  public function getAuthPassword() {
    return $this->password;
  }
  
  public function getReminderEmail() {
    return $this->email;
  }
  
  public function characters() {
    return $this->hasMany('Character');
  }
  
  public function securityQuestions() {
    return $this->hasMany('UserSecurityQuestion');
  }
  
  public function ips() {
    return $this->hasMany('UserIP');
  }
}