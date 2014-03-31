<?php

Route::group(['prefix' => 'auth'], function() {
  Route::post('/login', ['as' => 'auth.login', 'uses' => 'forum\AuthController@login']);
});

Route::get ('/login', ['as' => 'login', 'uses' => 'forum\ForumController@login']);