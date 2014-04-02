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
      <table class="forum pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>
            {{ HTML::linkAction('forum.category', $category->name, $category->path) }} >
            @foreach($forums as $f)
              {{ HTML::linkAction('forum.view', $f->name, [$category->path, $f->path]) }} >
            @endforeach
              @lang('forum.newpost.title')
            </th>
          </tr>
        </thead>
      </table>
      
      {{ Form::open(['route' => 'forum.topic.new', 'method' => 'PUT', 'class' => 'pure-form pure-form-stacked']) }}
      {{ Form::label('title', Lang::get('forum.newpost.title')) }}
      
      @if($errors->has('title'))
      {{ var_dump($errors->get('title')) }}
      @endif
      
      {{ Form::text('title', Input::old('title')) }}
      {{ Form::label('body', Lang::get('forum.newpost.body')) }}

      @if($errors->has('body'))
      {{ var_dump($errors->get('body')) }}
      @endif
      {{ Form::textarea('body', Input::old('title')) }}
      {{ Form::submit(Lang::get('forum.newpost.submit')) }}
      {{ Form::close() }}
      
    </section>
  </body>
</html>