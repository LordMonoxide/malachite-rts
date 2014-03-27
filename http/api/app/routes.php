<?php

Route::group(['prefix' => 'lang'], function() {
  Route::get('/app',  ['as' => 'lang.app',  'uses' => 'lang\LanguageController@app']);
  Route::get('/menu', ['as' => 'lang.menu', 'uses' => 'lang\LanguageController@menu']);
});

Route::group(['prefix' => 'auth'], function() {
  Route::get ('/check',    ['as' => 'auth.check',    'uses' => 'auth\AuthController@check']);
  Route::put ('/register', ['as' => 'auth.register', 'uses' => 'auth\AuthController@register']);
  Route::post('/login',    ['as' => 'auth.login',    'uses' => 'auth\AuthController@login']);
  Route::post('/logout',   ['as' => 'auth.logout',   'uses' => 'auth\AuthController@logout']);
  Route::get ('/security', ['as' => 'auth.security', 'uses' => 'auth\AuthController@security']);
  Route::post('/security', ['as' => 'auth.security', 'uses' => 'auth\AuthController@unlock']);
});

Route::group(['prefix' => 'storage'], function() {
  Route::group(['prefix' => 'characters'], function() {
    Route::get   ('/', ['as' => 'storage.characters', 'uses' => 'storage\CharacterController@all']);
    Route::put   ('/', ['as' => 'storage.characters', 'uses' => 'storage\CharacterController@create']);
    Route::delete('/', ['as' => 'storage.characters', 'uses' => 'storage\CharacterController@delete']);
    Route::post  ('/', ['as' => 'storage.characters', 'uses' => 'storage\CharacterController@choose']);
  });
});