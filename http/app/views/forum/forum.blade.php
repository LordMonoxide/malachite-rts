@extends('forum.layout')

<?php function renderForum($forums, $nest = 12) {
  if($forums === null) { return; }
  
  foreach($forums as $f) {
    echo '          <tr>
            <td style="padding-left:' . $nest . 'px">' . HTML::linkAction('forum.forum', $f->name, $f->id) . '</td>
          </tr>
';
    renderForum($f->children, $nest + 12);
  }
} ?>

@section('title')
@lang('forum.title')
@stop

@section('body')
  @unless(count($forum->children) === 0)
      <table class="forums pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>@lang('forum.name')</th>
          </tr>
        </thead>
        
        <tbody>
<?php renderForum($forum->children); ?>
        </tbody>
      </table>
  @endunless
      
      <table class="topics pure-table pure-table-horizontal pure-table-striped">
  @unless($forum->parent === null)
        <thead>
          <tr>
            <th>@lang('forum.title')</th>
            <th>@lang('forum.replies')</th>
            <th class="topic-newest-post">@lang('forum.newestpost')</th>
          </tr>
        </thead>
  @endunless
        
  @if(count($forum->topics) !== 0)
    @foreach($forum->topics()->newest()->get() as $topic)
        <tbody>
          <tr>
            <td class="topic-name">
              <img style="float:right;" src="{{ $topic->creator->avatar }}?s=40" alt="Avatar" />
              @lang('forum.topic.view.link', ['title' => $topic->title, 'forum' => $forum->id, 'name' => $topic->nameForUri, 'topic' => $topic->id, 'user' => $topic->creator->name, 'date' => $topic->created_at])
            </td>
            <td class="topic-post-count">{{ $topic->posts->count() }}</td>
            <td class="topic-newest-post">
              <?php $newest = $topic->posts()->newest()->first(); ?>
              @lang('forum.topic.newestpost', ['user' => $newest->author->name, 'date' => $newest->created_at])
            </td>
          </tr>
        </tbody>
    @endforeach
  @elseif($forum->parent !== null)
        <tbody>
          <tr>
            <td class="topic-name">@lang('forum.noposts')</td>
            <td></td>
            <td></td>
          </tr>
        </tbody>
  @endif
      </table>
      
  @unless($forum->parent === null)
      {{ HTML::linkAction('forum.topic.new', Lang::get('forum.topic.new'), $forum->id) }}
  @endunless
@stop