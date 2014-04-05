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

Route::group(['prefix' => 'auth'], function() {
  Route::post('login',  ['as' => 'auth.login',  'uses' => 'auth\AuthController@login']);
  Route::get ('logout', ['as' => 'auth.logout', 'uses' => 'auth\AuthController@logout']);
});

Route::group(['prefix' => 'forum'], function() {
  Route::model('forum', 'ForumForum');
  Route::model('topic', 'ForumTopic');
  
  Route::get('/',                             ['as' => 'forum.index',               'uses' => 'forum\ForumController@index']);
  Route::get('/f{forum}',                     ['as' => 'forum.forum',               'uses' => 'forum\ForumController@forum']);
  Route::get('/f{forum}/newtopic',            ['as' => 'forum.topic.new',           'uses' => 'forum\ForumController@newtopic']);
  Route::put('/f{forum}/newtopic',            ['as' => 'forum.topic.submit',        'uses' => 'forum\ForumController@submittopic']);
  Route::get('/f{forum}/{name}{topic}/reply', ['as' => 'forum.topic.reply2',        'uses' => 'forum\ForumController@replytopic']);
  Route::get('/f{forum}/{topic}/reply',       ['as' => 'forum.topic.reply',         'uses' => 'forum\ForumController@replytopic']);
  Route::put('/f{forum}/{topic}/reply',       ['as' => 'forum.topic.reply.submit',  'uses' => 'forum\ForumController@submitreply']);
  Route::get('/f{forum}/{name}{topic}',       ['as' => 'forum.topic.view2',         'uses' => 'forum\ForumController@viewtopic'])->where('name', '^[a-z\d-]+-$');
  Route::get('/f{forum}/{topic}',             ['as' => 'forum.topic.view',          'uses' => 'forum\ForumController@viewtopic']);
});

Route::get ('/',      ['as' => 'home',  'uses' => 'RootController@home']);
Route::get ('/login', ['as' => 'login', 'uses' => 'RootController@login']);