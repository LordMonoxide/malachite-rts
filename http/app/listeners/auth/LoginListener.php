<?php namespace auth;

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
      } else {
        $user->suspend = true;
        $user->save();
        Auth::logout();
      }
      
      $ip->save();
    } else {
      // De-auth user if the IP isn't authed
      if(!$ip->authorised) {
        $user->suspend = true;
        $user->save();
        Auth::logout();
      }
      
      // Update timestamps to show last login
      $ip->touch();
    }
  }
  
  public function onLogout($user) {
    $user->logged_in = false;
    $user->save();
  }
  
  public function subscribe($events) {
    $events->listen('auth.login',  'auth\LoginListener@onLogin');
    $events->listen('auth.logout', 'auth\LoginListener@onLogout');
  }
}