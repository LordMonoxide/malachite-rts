@extends('forum.layout')

@section('title')
@lang('forum.topic.view.title')
@stop

@section('breadcrumbs')
  <li>{{ HTML::linkAction('forum.topic.view2', $topic->title, [$forum->id, $topic->nameForUri, $topic->id]) }}</li>
  <li>></li>
@stop

@section('body')
  <table class="forum pure-table pure-table-horizontal pure-table-striped">
    <tbody>
      @foreach($topic->posts as $post)
        <tr>
          <td class="post-id">
            <span class="user-name">{{{ $post->author->name }}}</span><br />
            
            @unless($post->author->forumInfo->title === null)
              <span class="user-title">{{{ $post->author->forumInfo->title }}}</span><br />
            @endunless
            
            <img class="user-avatar" src="{{ $post->author->avatar }}?s=128" alt="{{ $post->author->name_first }}'s avatar" /><br />
            
            <div class="user-info">
              <span class="user-data">@lang('user.joined')</span>
              <span class="user-data">{{ $post->author->created_at->toFormattedDateString() }}</span>
              
              @unless($post->author->forumInfo->location === null)
                <span class="user-data">@lang('user.location')</span>
                <span class="user-data">{{{ $post->author->forumInfo->location }}}</span>
              @endunless
              
              <span class="user-data">@lang('user.posts')</span>
              <span class="user-data">{{ $post->author->forumInfo->post_count }}</span>
              
              <span class="user-data">@lang('user.rep')</span>
              <span class="user-data">{{ $post->author->forumInfo->rep }}</span>
            </div>
          </td>
          <td class="post-body">
            <cite class="post-citation">{{ $post->created_at }}</cite>
            
            {{ Form::open(['route' => ['forum.topic.rep.pos', $forum->id, $topic->id], 'method' => 'PUT', 'class' => 'post-rep-form']) }}
            {{ Form::submit(Lang::get('forum.topic.rep.pos')) }}
            {{ Form::close() }}
            
            {{ Form::open(['route' => ['forum.topic.rep.neg', $forum->id, $topic->id], 'method' => 'PUT', 'class' => 'post-rep-form']) }}
            {{ Form::submit(Lang::get('forum.topic.rep.neg')) }}
            {{ Form::close() }}
            
            <span class="post-rep-form">@lang('forum.topic.rep')</span>
            
            <hr />
            {{{ $post->body }}}
          </td>
        </tr>
      @endforeach
    </tbody>
  </table>
      
  {{ HTML::linkAction('forum.topic.reply2', Lang::get('forum.topic.reply'), [$forum->id, $topic->nameForUri, $topic->id]) }}
@stop