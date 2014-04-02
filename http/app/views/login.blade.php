<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('menu.login.title') }}</title>
    {{ HTML::style('assets/css/main.css') }}
    {{ HTML::script('//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js') }}
    {{ HTML::script('assets/js/home.js', ['async']) }}
  </head>
  
  <body>
    <div class="true-center-outer">
      <div class="true-center-inner">
        <div id="login-div" class="true-center-content">
          {{ Form::hidden('route_dest',     URL::previous()) }}
          {{ Form::hidden('route_login',    URL::route('api.auth.login')) }}
          {{ Form::hidden('route_register', URL::route('api.auth.register')) }}
          
          {{ Form::open(['route' => 'auth.login', 'method' => 'POST', 'id' => 'login-form', 'class' => 'pure-form pure-form-stacked']) }}
          <legend>@lang('app.title')</legend>
          
          {{ Form::email('email', Input::old('email'), ['placeholder' => Lang::get('auth.email'),            'required' => 'required', 'autofocus' => 'autofocus']) }}
          {{ Form::password('password',                ['placeholder' => Lang::get('auth.password'),         'required' => 'required']) }}
          {{ Form::password('password_confirmation',   ['placeholder' => Lang::get('auth.password_confirm'), 'required' => 'required', 'disabled' => 'disabled', 'style' => 'display: none;']) }}
          
          <label>
            {{ Form::checkbox('newaccount', null, false) }}
            @lang('auth.newaccount')
          </label>
          
          {{ Form::submit(Lang::get('auth.submit'), ['id' => 'login', 'class' => 'pure-button pure-button-primary']) }}
          {{ Form::close() }}
        </div>
      </div>
    </div>
  </body>
</html>