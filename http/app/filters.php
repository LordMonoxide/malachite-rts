<?php

/*
|--------------------------------------------------------------------------
| Application & Route Filters
|--------------------------------------------------------------------------
|
| Below you will find the "before" and "after" events for the application
| which may be used to do any work before or after a request into your
| application. Here you may also register your custom route filters.
|
*/

App::before(function($request) {
	
});


App::after(function($request, $response) {
	
});

/*
|--------------------------------------------------------------------------
| Authentication Filters
|--------------------------------------------------------------------------
|
| The following filters are used to verify that the user of the current
| session is logged into this application. The "basic" filter easily
| integrates HTTP Basic authentication for quick, simple checking.
|
*/

Route::filter('auth', function() {
  if(Auth::guest()) {
    return Redirect::guest('login');
  }
});

Route::filter('nauth', function() {
  if(Auth::check()) {
    return Redirect::home();
  }
});

Route::filter('auth.401', function() {
  if(Auth::check()) {
    if(!Auth::user()->logged_in) {
      Auth::logout();
    }
  }
  
  if(Auth::guest()) {
    return Response::json([
      'error' => 'loginrequired',
      'show'  => 'login'
    ], 401);
  }
});

Route::filter('nauth.409', function() {
  if(Auth::check()) {
    return Response::json(['error' => ['Already logged in.']], 409);
  }
});

Route::filter('auth.basic', function() {
  return Auth::basic();
});

Route::filter('user.security', function() {
  if(Auth::check()) {
    if(!Auth::user()->logged_in) {
      Auth::logout();
    }
  }
  
  if(Auth::guest()) {
    return Response::json([
      'error' => 'loginrequired',
      'show'  => 'login'
    ], 401);
  }
  
  if(Auth::user()->suspend) {
    $q = [];
    Auth::user()->securityQuestions()->get()->each(function($question) use(&$q) {
      $q[] = $question->question;
    });
    
    return Response::json([
      'error'     => 'security',
      'show'      => 'security',
      'questions' => $q
    ], 401);
  }
});

/*
|--------------------------------------------------------------------------
| Guest Filter
|--------------------------------------------------------------------------
|
| The "guest" filter is the counterpart of the authentication filters as
| it simply checks that the current user is not logged in. A redirect
| response will be issued if they are, which you may freely change.
|
*/

Route::filter('guest', function() {
  if(Auth::check()) {
    return Redirect::route('characters.index');
  }
});

/*
|--------------------------------------------------------------------------
| CSRF Protection Filter
|--------------------------------------------------------------------------
|
| The CSRF filter is responsible for protecting your application against
| cross-site request forgery attacks. If this special token in a user
| session does not match the one given in this request, we'll bail.
|
*/

Route::filter('csrf', function() {
  if(Session::token() != Input::get('_token')) {
    throw new Illuminate\Session\TokenMismatchException;
  }
});