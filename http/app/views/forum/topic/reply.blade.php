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
          <li>{{ HTML::linkAction('forum.category', $category->name, $category->path) }}</li>
          <li>></li>
          @foreach($forums as $f)
          <li>{{ HTML::linkAction('forum.view', $f->name, [$category->path, $f->path]) }}</li>
          <li>></li>
          @endforeach
          <li>{{ HTML::linkAction('forum.view', $topic->title, [$category->path, $topic->path]) }}</li>
          <li>></li>
          <li>@lang('forum.topic.reply')</li>
          <li>></li>
        </ul>
      </div>
      
      {{ Form::open(['route' => 'forum.topic.reply', 'method' => 'PUT', 'class' => 'pure-form pure-form-stacked']) }}
      {{ Form::label('body', Lang::get('forum.topic.reply.body')) }}
      @if($errors->has('body'))
      {{ var_dump($errors->get('body')) }}
      @endif
      {{ Form::textarea('body', Input::old('title')) }}
      {{ Form::submit(Lang::get('forum.topic.reply.submit')) }}
      {{ Form::close() }}
      
    </section>
  </body>
</html>