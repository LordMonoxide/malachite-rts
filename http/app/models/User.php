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
  
  public function getRememberToken() {
    return $this->remember_token;
  }
  
  public function setRememberToken($value) {
    $this->remember_token = $value;
  }
  
  public function getRememberTokenName()  {
    return 'remember_token';
  }
  
  public function info() {
    return $this->hasOne('UserInfo');
  }
  
  public function reps() {
    return $this->hasMany('UserPostRep');
  }
  
  public function securityQuestions() {
    return $this->hasMany('UserSecurityQuestion');
  }
  
  public function ips() {
    return $this->hasMany('UserIP');
  }
  
  public function getNameAttribute() {
    return $this->name_first . ' ' . $this->name_last;
  }
  
  public function getAvatarAttribute() {
    return "http://www.gravatar.com/avatar/" . md5(strtolower(trim($this->email)));
  }
}