<?php

Route::group(['prefix' => 'api'], function() {
  Route::group(['prefix' => 'lang'], function() {
    Route::get('/app',  ['as' => 'api.lang.app',  'uses' => 'api\lang\LanguageController@app']);
    Route::get('/menu', ['as' => 'api.lang.menu', 'uses' => 'api\lang\LanguageController@menu']);
  });

  Route::group(['prefix' => 'auth'], function() {
    Route::get ('/check',    ['as' => 'api.auth.check',    'uses' => 'api\auth\AuthController@check']);
    Route::put ('/register', ['as' => 'api.auth.register', 'uses' => 'api\auth\AuthController@register']);
    Route::post('/login',    ['as' => 'api.auth.login',    'uses' => 'api\auth\AuthController@login']);
    Route::post('/logout',   ['as' => 'api.auth.logout',   'uses' => 'api\auth\AuthController@logout']);
    Route::get ('/security', ['as' => 'api.auth.security', 'uses' => 'api\auth\AuthController@security']);
    Route::post('/security', ['as' => 'api.auth.unlock',   'uses' => 'api\auth\AuthController@unlock']);
  });

  Route::group(['prefix' => 'storage'], function() {
    Route::group(['prefix' => 'characters'], function() {
      Route::get   ('/', ['as' => 'api.storage.characters.all',    'uses' => 'api\storage\CharacterController@all']);
      Route::put   ('/', ['as' => 'api.storage.characters.create', 'uses' => 'api\storage\CharacterController@create']);
      Route::delete('/', ['as' => 'api.storage.characters.delete', 'uses' => 'api\storage\CharacterController@delete']);
      Route::post  ('/', ['as' => 'api.storage.characters.choose', 'uses' => 'api\storage\CharacterController@choose']);
    });
    
    Route::group(['prefix' => 'races'], function() {
      Route::get('/', ['as' => 'api.storage.races.all', 'uses' => 'api\storage\RaceController@all']);
    });
  });
});