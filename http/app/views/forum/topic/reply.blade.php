@extends('forum.layout.layout')

@section('title')
  @lang('forum.topic.reply.title')
@stop

@section('body')
  <div class="posts">
    <table class="posts pure-table pure-table-horizontal pure-table-striped">
      <tbody>
        <tr>
          <td>
            {{ Form::open(['route' => ['forum.topic.reply.submit', $forum->id, $topic->id], 'method' => 'PUT', 'class' => 'pure-form pure-form-stacked']) }}
            {{ Form::label('body', Lang::get('forum.topic.reply.body')) }}
            @if($errors->has('body'))
            {{ var_dump($errors->get('body')) }}
            @endif
            {{ Form::textarea('body', Input::old('title')) }}
            {{ Form::submit(Lang::get('forum.topic.reply.submit')) }}
            {{ Form::close() }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
@stop