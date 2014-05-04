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
      <div class="breadcrumbs pure-menu pure-menu-open pure-menu-horizontal">
        <ul>
          <li>{{ HTML::linkAction('forum.index', Lang::get('forum.index')) }}</li>
          <li>></li>

<?php
  if(isset($forum)) {
    $f = $forum;
    $fs = [];
    
    while($f != null) {
      array_unshift($fs, $f);
      $f = $f->parent;
    }
    
    foreach($fs as $f) {
      echo '          <li>' . HTML::linkAction('forum.forum', $f->name, $f->id) . '</li>
          <li>></li>';
    }
  }
?>

@yield('breadcrumbs')
        </ul>
        
        <ul style="float:right;">
          <li><a href="{{ URL::action('forum.settings') }}">{{ HTML::image('assets/img/forum/settings.png', Lang::get('forum.settings'), ['class' => 'icon']) }}</a></li>
          <li>{{ HTML::linkAction('auth.logout', Lang::get('app.logout')) }}</li>
        </ul>
      </div>
    </section>
    
    <section>
@yield('body')
    </section>
  </body>
</html>