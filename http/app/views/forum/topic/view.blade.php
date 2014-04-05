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
      
      {{ HTML::linkAction('forum.topic.reply2', Lang::get('forum.topic.reply'), [$forum->id, $topic->nameForUri, $topic->id]) }}
@stop