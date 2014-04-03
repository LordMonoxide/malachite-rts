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
          <li>{{ HTML::linkAction('forum.index', Lang::get('forum.index')) }}</li>
          <li>></li>
          <li>{{ HTML::linkAction('forum.category', $category->name, $category->path) }}</li>
          <li>></li>
          @foreach($forums as $f)
          <li>{{ HTML::linkAction('forum.view', $f->name, [$category->path, $f->path]) }}</li>
          <li>></li>
          @endforeach
        </ul>
      </div>
      
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
            <th>@lang('forum.replies')</th>
            <th class="topic-newest-post">@lang('forum.newestpost')</th>
          </tr>
        </thead>
        
        <tbody>
          @if(count($forum->topics) != 0)
          @foreach($forum->topics as $topic)
          <tr>
            <td class="topic-name">
              <img style="float:right;" src="{{ $topic->creator->avatar }}?s=40" alt="Avatar" />
              @lang('forum.topic.link', ['title' => $topic->title, 'category' => $category->path, 'topic' => $topic->path, 'user' => $topic->creator->name, 'date' => $topic->created_at])
            </td>
            <td class="topic-post-count">{{ $topic->posts->count() }}</td>
            <td class="topic-newest-post">
              <?php $newest = $topic->posts()->newest()->first(); ?>
              @lang('forum.topic.newestpost', ['user' => $newest->author->name, 'date' => $newest->created_at])
            </td>
          </tr>
          @endforeach
          @else
          <tr>
            <td class="topic-name">@lang('forum.noposts')</td>
            <td></td>
            <td></td>
          </tr>
          @endif
        </tbody>
      </table>
      
      {{ HTML::linkAction('forum.view', Lang::get('forum.new'), [$category->path, $forum->path . '/new']) }}
    </section>
  </body>
</html>