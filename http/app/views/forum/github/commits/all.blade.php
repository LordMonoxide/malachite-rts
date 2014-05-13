@extends('forum.layout.layout')

@section('title')
  @lang('forum.github.commits.title')
@stop

@section('body')
  <div class="commits">
    @foreach($pushes as $push)
      <table class="commits pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>@lang('forum.github.commits.commit.message')</th>
            <th>@lang('forum.github.commits.commit.username')</th>
          </tr>
        </thead>
        
        <tbody>
          <tr class="push">
            <td class="commit-hash">{{ HTML::link($push->compare, $push->before . '...' . $push->after) }}</td>
            <td class="commit-pusher">{{ $push->pusher_name }}</td>
          </tr>
          
          @foreach($push->commits as $commit)
            <tr class="commit">
              <td class="commit-message">{{ HTML::link($commit->url, $commit->message) }}</td>
              <td class="commit-author">{{ $commit->author_username }}</td>
            </tr>
          @endforeach
        </tbody>
      </table>
    @endforeach
  </div>
@stop