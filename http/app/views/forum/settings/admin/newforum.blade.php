@extends('forum.settings.layout')

@section('settings')
  {{ Form::open(['route' => ['forum.settings.admin.forums.new.submit'], 'method' => 'PUT', 'class' => 'pure-form pure-form-stacked']) }}
  
  @if($parent !== null)
    {{ Form::hidden('parent', $parent->id) }}
    <legend>@lang('forum.settings.admin.forums.new.legend.parent', ['parent' => $parent->name])</legend>
  @else
    <legend>@lang('forum.settings.admin.forums.new.legend.root')</legend>
  @endif
  
  {{ Form::label('name', Lang::get('forum.settings.admin.forums.new.name')) }}
  
  @if($errors->has('name'))
    {{ var_dump($errors->get('name')) }}
  @endif
  
  {{ Form::text('name', Input::old('name')) }}
  {{ Form::submit(Lang::get('forum.settings.admin.forums.new.submit')) }}
  {{ Form::close() }}
@stop