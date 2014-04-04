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
    <section>
      <div class="pure-menu pure-menu-open pure-menu-horizontal">
        <ul>
@yield('breadcrumbs')
        </ul>
        
        <ul style="float:right;">
          <li>{{ HTML::linkAction('auth.logout', Lang::get('app.logout')) }}</li>
        </ul>
      </div>
    </section>
    
    <section>
@yield('body')
    </section>
  </body>
</html>