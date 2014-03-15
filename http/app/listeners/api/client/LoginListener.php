<?php namespace api\client;

use Request;

use UserIP;

class LoginListener {
  public function onLogin($user) {
    $user->logged_in = true;
    $user->save();
    
    // Look for a UserIP for the user's IP
    $ip = $user->ips()->where('ip', '=', ip2long(Request::getClientIp()))->first();
    
    if($ip === null) {
      // Create a new one if none exists
      $ip = new UserIP;
      $ip->user()->associate($user);
      $ip->ip = ip2long(Request::getClientIp());
      
      // Automatically auth the IP if it's the
      // first one that they've logged in with
      if($user->ips->count() === 0) {
        $ip->authorised = true;
      }
      
      $ip->save();
    } else {
      // Update timestamps to show last login
      $ip->touch();
    }
  }
  
  public function onLogout($user) {
    $user->logged_in = false;
    $user->save();
    
    // Remove the auth token for all chars
    foreach($user->characters as $char) {
      $char->auth_id = null;
      $char->save();
    }
  }
  
  public function subscribe($events) {
    $events->listen('auth.login',  'api\client\LoginListener@onLogin');
    $events->listen('auth.logout', 'api\client\LoginListener@onLogout');
  }
}