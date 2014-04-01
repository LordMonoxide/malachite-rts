<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('menu.login.title') }}</title>
  </head>
  
  <body>
    {{ Form::open(['route' => 'auth.login', 'method' => 'POST']) }}
    {{ Form::label('email', Lang::get('menu.login.email')) }}
    {{ Form::email('email') }}
    {{ Form::label('password', Lang::get('menu.login.pass')) }}
    {{ Form::password('password') }}
    {{ Form::submit(Lang::get('menu.login.login')) }}
    {{ Form::close() }}
  </body>
</html>