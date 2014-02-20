<?php

Route::group(['prefix' => 'api'], function() {
  Route::group(['prefix' => 'client'], function() {
    Route::group(['prefix' => 'menu'], function() {
      Route::get ('/check',    ['as' => 'api.client.menu.check',    'uses' => 'api\client\AuthController@check']);
      Route::put ('/register', ['as' => 'api.client.menu.register', 'uses' => 'api\client\AuthController@register']);
      Route::post('/login',    ['as' => 'api.client.menu.login',    'uses' => 'api\client\AuthController@login']);
      Route::post('/logout',   ['as' => 'api.client.menu.logout',   'uses' => 'api\client\AuthController@logout']);
      Route::get ('/security', ['as' => 'api.client.menu.security', 'uses' => 'api\client\AuthController@security']);
      Route::post('/security', ['as' => 'api.client.menu.security', 'uses' => 'api\client\AuthController@unlock']);
    });
  });
});