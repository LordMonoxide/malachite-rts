<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('forum.newpost.title') }}</title>
    {{ HTML::style('assets/css/main.css') }}
    {{ HTML::style('assets/css/forum.css') }}
    {{ HTML::script('//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js') }}
  </head>
  
  <body>
    <section>
      <div class="pure-menu pure-menu-open pure-menu-horizontal">
        <ul>
          <li>{{ HTML::linkAction('forum.index', Lang::get('forum.index')) }}</li>
          <li>></li>
          <li>{{ HTML::linkAction('forum.category', $category->name, $category->path) }}</li>
          <li>></li>
          @foreach($forums as $f)
          <li>{{ HTML::linkAction('forum.view', $f->name, [$category->path, $f->path]) }}</li>
          <li>></li>
          @endforeach
          <li>@lang('forum.newpost.title')</li>
          <li>></li>
        </ul>
      </div>
      
      {{ Form::open(['route' => 'forum.topic.new', 'method' => 'PUT', 'class' => 'pure-form pure-form-stacked']) }}
      {{ Form::label('title', Lang::get('forum.newpost.post.title')) }}
      
      @if($errors->has('title'))
      {{ var_dump($errors->get('title')) }}
      @endif
      
      {{ Form::text('title', Input::old('title')) }}
      {{ Form::label('body', Lang::get('forum.newpost.post.body')) }}

      @if($errors->has('body'))
      {{ var_dump($errors->get('body')) }}
      @endif
      {{ Form::textarea('body', Input::old('title')) }}
      {{ Form::submit(Lang::get('forum.newpost.post.submit')) }}
      {{ Form::close() }}
    </section>
  </body>
</html>