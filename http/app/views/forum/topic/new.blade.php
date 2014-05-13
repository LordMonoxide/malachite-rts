@extends('forum.layout.layout')

@section('title')
  @lang('forum.topic.new.title')
@stop

@section('body')
  <div class="posts">
    <table class="posts pure-table pure-table-horizontal pure-table-striped">
      <tbody>
        <tr>
          <td>
            {{ Form::open(['route' => ['forum.topic.submit', $forum->id], 'method' => 'PUT', 'class' => 'pure-form pure-form-stacked']) }}
            {{ Form::label('title', Lang::get('forum.newpost.post.title')) }}
            
            @if($errors->has('title'))
            {{ var_dump($errors->get('title')) }}
            @endif
            
            {{ Form::text('title', Input::old('title')) }}
            {{ Form::label('body', Lang::get('forum.newpost.post.body')) }}
            
            @if($errors->has('body'))
            {{ var_dump($errors->get('body')) }}
            @endif
            {{ Form::textarea('body', Input::old('title')) }}
            {{ Form::submit(Lang::get('forum.newpost.post.submit')) }}
            {{ Form::close() }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
@stop