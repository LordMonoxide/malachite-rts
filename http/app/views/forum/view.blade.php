<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('forum.title') }}</title>
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
            </th>
          </tr>
        </thead>
      </table>
      
      @if($forum->children()->count() != 0)
      <table class="forum pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>@lang('forum.name')</th>
          </tr>
        </thead>
        
        <tbody>
          @foreach($forum->children as $child)
          <tr>
            <td>{{ HTML::linkAction('forum.view', $child->name, [$category->path, $child->path]) }}</td>
          </tr>
          @endforeach
        </tbody>
      </table>
      @endif
      
      <table class="forum pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>@lang('forum.title')</th>
            <th>@lang('forum.user')</th>
          </tr>
        </thead>
        
        <tbody>
          @foreach($forum->posts as $post)
          <tr>
            <td>{{ HTML::linkAction('forum.view', $post->title, [$category->path, $forum->path . '/post/' . $post->id]) }}</td>
            <td>{{{ $post->creator->name_first }}} {{{ $post->creator->name_last }}}</td>
          </tr>
          @endforeach
        </tbody>
      </table>
      
      {{ HTML::linkAction('forum.view', Lang::get('forum.new'), [$category->path, $forum->path . '/new']) }}
    </section>
  </body>
</html>