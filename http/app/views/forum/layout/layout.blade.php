<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - @yield('title')</title>
    {{ HTML::style('assets/css/main.css') }}
    {{ HTML::style('assets/css/forum.css') }}
    {{ HTML::script('//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js') }}
  </head>
  
  <body>
    <section class="header pure-menu pure-menu-open pure-menu-horizontal">
      <ul class="header">
        <li>@lang('forum.welcome', ['name' => Auth::user()->name_first])</li>
      </ul>
      
      <ul class="settings">
        <li>{{ HTML::linkAction('forum.settings', '', null, ['class' => 'icon logout']) }}</li>
        <li>{{ HTML::linkAction('auth.logout', Lang::get('app.logout')) }}</li>
      </ul>
    </section>
    
    <section>
      <div class="pure-g">
        <div class="pure-u-1-5">
          @include('forum.layout.nav', ['forums' => $forums, 'forum' => isset($forum) ? $forum : null])
        </div>
        
        <div class="pure-u-4-5">
          @yield('body')
        </div>
      </div>
    </section>
  </body>
</html>