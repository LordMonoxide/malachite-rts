@extends('forum.layout')

@section('title')
@lang('forum.topic.view.title')
@stop

@section('breadcrumbs')
          <li>{{ HTML::linkAction('forum.topic.view2', $topic->title, [$forum->id, $topic->nameForUri, $topic->id]) }}</li>
          <li>></li>
@stop

@section('body')
      {{ Form::open(['route' => ['forum.topic.reply.submit', $forum->id, $topic->id], 'method' => 'PUT', 'class' => 'pure-form pure-form-stacked']) }}
      {{ Form::label('body', Lang::get('forum.topic.reply.body')) }}
      @if($errors->has('body'))
      {{ var_dump($errors->get('body')) }}
      @endif
      {{ Form::textarea('body', Input::old('title')) }}
      {{ Form::submit(Lang::get('forum.topic.reply.submit')) }}
      {{ Form::close() }}
@stop