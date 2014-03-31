<!DOCTYPE html>

<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>{{ Lang::get('app.title') }} - {{ Lang::get('login.title') }}</title>
</head>

<body>
  {{ Form::open(['route' => 'auth.login', 'method' => 'post']) }}
  {{ Form::label('email', Lang::get('login.email')) }}
  {{ Form::email('email') }}<br />
  {{ Form::label('password', Lang::get('login.password')) }}
  {{ Form::password('password') }}<br />
  {{ Form::submit() }}
  {{ Form::close() }}
</body>
</html>