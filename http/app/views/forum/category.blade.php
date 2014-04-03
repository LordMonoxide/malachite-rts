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
      <div class="pure-menu pure-menu-open pure-menu-horizontal">
        <ul>
          <li>{{ HTML::linkAction('forum.category', $category->name, $category->path) }}</li>
        </ul>
      </div>
      
      <table class="forum pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>{{ HTML::linkAction('forum.category', $category->name, $category->path) }}</th>
          </tr>
        </thead>
        
        <tbody>
        @foreach($category->forums as $forum)
          <tr>
            <td>{{ HTML::linkAction('forum.view', $forum->name, [$category->path, $forum->path]) }}</td>
          </tr>
        @endforeach
        </tbody>
      </table>
    </section>
  </body>
</html>