<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('forum.viewpost.title') }}</title>
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
              {{{ $topic->title }}}
            </th>
          </tr>
        </thead>
      </table>
    </section>
  </body>
</html>