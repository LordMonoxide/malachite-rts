@extends('tech.layout')

@section('body')
  @include('tech.section', ['type' => 'buildings', 'data' => $buildings])
@stop