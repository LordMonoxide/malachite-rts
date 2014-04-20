@extends('tech.layout')

@section('body')
  @include('tech.section', ['type' => 'buildings', 'data' => $buildings])
  @include('tech.section', ['type' => 'research',  'data' => $research])
  @include('tech.section', ['type' => 'units',     'data' => $units])
@stop