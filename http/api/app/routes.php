<?php

Route::group(['prefix' => 'auth'], function() {
  Route::get ('/check',    ['as' => 'auth.check',    'uses' => 'auth\AuthController@check']);
  Route::put ('/register', ['as' => 'auth.register', 'uses' => 'auth\AuthController@register']);
  Route::post('/login',    ['as' => 'auth.login',    'uses' => 'auth\AuthController@login']);
  Route::post('/logout',   ['as' => 'auth.logout',   'uses' => 'auth\AuthController@logout']);
  Route::get ('/security', ['as' => 'auth.security', 'uses' => 'auth\AuthController@security']);
  Route::post('/security', ['as' => 'auth.security', 'uses' => 'auth\AuthController@unlock']);
});