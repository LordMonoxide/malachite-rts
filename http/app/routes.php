<?php

Route::group(['prefix' => 'api'], function() {
  Route::group(['prefix' => 'lang'], function() {
    Route::get('/app',  ['as' => 'api.lang.app',  'uses' => 'api\lang\LanguageController@app']);
    Route::get('/menu', ['as' => 'api.lang.menu', 'uses' => 'api\lang\LanguageController@menu']);
    Route::get('/game', ['as' => 'api.lang.game', 'uses' => 'api\lang\LanguageController@game']);
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
    
    Route::group(['prefix' => 'settings'], function() {
      Route::get('/', ['as' => 'api.storage.settings.all', 'uses' => 'api\storage\SettingsController@all']);
    });
    
    Route::group(['prefix' => 'tech'], function() {
      Route::get('/buildings', ['as' => 'api.storage.tech.buildings.all', 'uses' => 'api\storage\TechController@buildings']);
      Route::get('/research',  ['as' => 'api.storage.tech.research.all',  'uses' => 'api\storage\TechController@research']);
    });
  });
  
  Route::group(['prefix' => 'github'], function() {
    Route::post('/hooks', ['as' => 'api.github.hooks', 'uses' => 'api\github\HooksController@dispatch']);
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
  
  Route::group(['prefix' => 'settings'], function() {
    Route::get('/',         ['as' => 'forum.settings',          'uses' => 'forum\SettingsController@settings']);
    Route::get('/personal', ['as' => 'forum.settings.personal', 'uses' => 'forum\SettingsController@personal']);
    Route::get('/account',  ['as' => 'forum.settings.account',  'uses' => 'forum\SettingsController@account']);
    
    Route::group(['prefix' => 'admin'], function() {
      Route::group(['prefix' => 'forums'], function() {
        Route::get('/',               ['as' => 'forum.settings.admin.forums',            'uses' => 'forum\SettingsController@forums']);
        Route::get('/new',            ['as' => 'forum.settings.admin.forums.new',        'uses' => 'forum\SettingsController@newForum']);
        Route::put('/new',            ['as' => 'forum.settings.admin.forums.new.submit', 'uses' => 'forum\SettingsController@submitForum']);
        Route::get('/{forum}/new',    ['as' => 'forum.settings.admin.forums.new.sub',    'uses' => 'forum\SettingsController@newForum']);
        Route::get('/{forum}/edit',   ['as' => 'forum.settings.admin.forums.edit',       'uses' => 'forum\SettingsController@editForum']);
        Route::get('/{forum}/delete', ['as' => 'forum.settings.admin.forums.delete',     'uses' => 'forum\SettingsController@deleteForum']);
      });
    });
  });
  
  Route::group(['prefix' => 'github'], function() {
    Route::group(['prefix' => 'commits'], function() {
      Route::get('/', ['as' => 'forum.github.commits.all', 'uses' => 'forum\github\CommitsController@all']);
    });
    
    Route::group(['prefix' => 'issues'], function() {
      Route::get('/', ['as' => 'forum.github.issues.all', 'uses' => 'forum\github\IssuesController@all']);
    });
    
    Route::get('/', ['as' => 'forum.github.home', 'uses' => 'forum\github\GitHubController@home']);
  });
  
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
  
  Route::get('/',                     ['as' => 'tech.all',            'uses' => 'tech\TechController@all']);
  Route::get('/buildings',            ['as' => 'tech.buildings.all',  'uses' => 'tech\TechController@buildings']);
  Route::get('/buildings/{building}', ['as' => 'tech.buildings.view', 'uses' => 'tech\TechController@building']);
  Route::get('/research',             ['as' => 'tech.research.all',   'uses' => 'tech\TechController@researches']);
  Route::get('/research/{research}',  ['as' => 'tech.research.view',  'uses' => 'tech\TechController@research']);
});

Route::get ('/',      ['as' => 'home',  'uses' => 'RootController@home']);
Route::get ('/login', ['as' => 'login', 'uses' => 'RootController@login']);