<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('index.title') }}</title>
    {{ HTML::style('assets/css/main.css') }}
  </head>
  
  <body>
    <p>{{ HTML::linkAction('forum.index', Lang::get('forum.title')) }}</p>
    
    @if(Auth::check())
      <p>{{ HTML::linkAction('tech.all', Lang::get('tech.title')) }}</p>
      <p>{{ HTML::linkAction('auth.logout', Lang::get('app.logout')) }}</p>
    @else
      <p>{{ HTML::linkAction('login', Lang::get('app.login')) }}</p>
    @endif
  </body>
</html>