@extends('forum.layout.layout')

@section('title')
  {{ $forum->name }}
@stop

@section('body')
  <div class="topics">
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
        <tbody>
          @foreach($forum->topics()->newest()->get() as $topic)
            <tr>
              <td class="topic-name">
                <img style="float:right;" src="{{ $topic->creator->avatar }}?s=40" alt="Avatar" />
                @lang('forum.topic.view.link', ['title' => $topic->title, 'forum' => $forum->id, 'name' => $topic->nameForUri, 'topic' => $topic->id, 'user' => $topic->creator->name, 'date' => $topic->created_at->diffForHumans(), 'fulldate' => $topic->created_at])
              </td>
              <td class="topic-post-count">{{ $topic->posts->count() }}</td>
              <td class="topic-newest-post">
                <?php $newest = $topic->posts()->newest()->first(); ?>
                @unless($newest === null)
                  @lang('forum.topic.newestpost', ['user' => $newest->author->name, 'date' => $newest->created_at->diffForHumans(), 'fulldate' => $newest->created_at])
                @endif
              </td>
            </tr>
          @endforeach
        </tbody>
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
      {{ HTML::linkAction('forum.topic.new', Lang::get('forum.topic.new'), $forum->id, ['class' => 'pure-button']) }}
    @endunless
  </div>
@stop