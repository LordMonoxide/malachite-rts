<?php

Route::group(['prefix' => 'referral'], function() {
  Route::post('/auth/login', ['as' => 'auth.login', 'uses' => 'forum\AuthController@login']);
});

Route::get ('/login', ['as' => 'login', 'uses' => 'forum\ForumController@login']);