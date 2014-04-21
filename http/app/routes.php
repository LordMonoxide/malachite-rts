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
    Route::group(['prefix' => 'news'], function() {
      Route::get('/',       ['as' => 'api.storage.news.all',    'uses' => 'api\storage\NewsController@all']);
      Route::get('/latest', ['as' => 'api.storage.news.latest', 'uses' => 'api\storage\NewsController@latest']);
    });
    
    Route::group(['prefix' => 'tech'], function() {
      Route::get('/buildings', ['as' => 'api.storage.tech.buildings.all', 'uses' => 'api\storage\TechController@buildings']);
      Route::get('/research',  ['as' => 'api.storage.tech.research.all',  'uses' => 'api\storage\TechController@research']);
    });
  });
});

Route::group(['prefix' => 'auth'], function() {
  Route::post('login',  ['as' => 'auth.login',  'uses' => 'auth\AuthController@login']);
  Route::get ('logout', ['as' => 'auth.logout', 'uses' => 'auth\AuthController@logout']);
});

Route::group(['prefix' => 'forum'], function() {
  Route::model('forum', 'Forum');
  Route::model('topic', 'Topic');
  Route::model('post',  'Post');
  
  Route::get('/',                             ['as' => 'forum.index',              'uses' => 'forum\ForumController@index']);
  Route::get('/f{forum}',                     ['as' => 'forum.forum',              'uses' => 'forum\ForumController@forum']);
  Route::get('/f{forum}/newtopic',            ['as' => 'forum.topic.new',          'uses' => 'forum\ForumController@newtopic']);
  Route::put('/f{forum}/newtopic',            ['as' => 'forum.topic.submit',       'uses' => 'forum\ForumController@submittopic']);
  Route::get('/f{forum}/{name}{topic}/reply', ['as' => 'forum.topic.reply2',       'uses' => 'forum\ForumController@replytopic']);
  Route::get('/f{forum}/{topic}/reply',       ['as' => 'forum.topic.reply',        'uses' => 'forum\ForumController@replytopic']);
  Route::put('/f{forum}/{topic}/reply',       ['as' => 'forum.topic.reply.submit', 'uses' => 'forum\ForumController@submitreply']);
  Route::get('/f{forum}/{name}{topic}',       ['as' => 'forum.topic.view2',        'uses' => 'forum\ForumController@viewtopic'])->where('name', '^[a-z\d-]+-$');
  Route::get('/f{forum}/{topic}',             ['as' => 'forum.topic.view',         'uses' => 'forum\ForumController@viewtopic']);
  
  Route::put('/p{post}/rep/pos', ['as' => 'forum.post.rep.pos', 'uses' => 'forum\PostController@reppos']);
  Route::put('/p{post}/rep/neg', ['as' => 'forum.post.rep.neg', 'uses' => 'forum\PostController@repneg']);
});

Route::group(['prefix' => 'tech'], function() {
  Route::model('building', 'Building');
  Route::model('research', 'Research');
  Route::model('unit',     'Unit');
  
  Route::get('/',                     ['as' => 'tech.all',            'uses' => 'tech\TechController@all']);
  Route::get('/buildings',            ['as' => 'tech.buildings.all',  'uses' => 'tech\TechController@buildings']);
  Route::get('/buildings/{building}', ['as' => 'tech.buildings.view', 'uses' => 'tech\TechController@building']);
  Route::get('/research',             ['as' => 'tech.research.all',   'uses' => 'tech\TechController@researches']);
  Route::get('/research/{research}',  ['as' => 'tech.research.view',  'uses' => 'tech\TechController@research']);
  Route::get('/units',                ['as' => 'tech.units.all',      'uses' => 'tech\TechController@units']);
  Route::get('/units/{unit}',         ['as' => 'tech.units.view',     'uses' => 'tech\TechController@unit']);
});

Route::get ('/',      ['as' => 'home',  'uses' => 'RootController@home']);
Route::get ('/login', ['as' => 'login', 'uses' => 'RootController@login']);