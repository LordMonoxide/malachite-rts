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
            {{{ $forum->category->name }}} >
            @foreach($forums as $f)
              {{{ $f->name }}} >
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
            <td>{{{ $child->name }}}</td>
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
            <td>{{{ $post->title }}}</td>
            <td>{{{ $post->creator->name_first }}} {{{ $post->creator->name_last }}}</td>
          </tr>
          @endforeach
        </tbody>
      </table>
    </section>
  </body>
</html>