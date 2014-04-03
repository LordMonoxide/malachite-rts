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
        </ul>
      </div>
    </section>
    
    <section>
      <table class="forum pure-table pure-table-horizontal pure-table-striped">
        <tbody>
          @foreach($topic->posts as $post)
          <tr>
            <td class="post-id">
              {{{ $post->author->name }}}<br />
              <img src="{{ $post->author->avatar }}?s=128" alt="{{ $post->author->name_first }}'s avatar" />
            </td>
            <td class="post-body">
              <cite class="post-citation">{{ $post->created_at }}</cite>
              <hr />
              {{{ $post->body }}}
            </td>
          </tr>
          @endforeach
        </tbody>
      </table>
      
      {{ HTML::linkAction('forum.view', Lang::get('forum.reply'), [$category->path, $topic->path . '/reply']) }}
    </section>
  </body>
</html>