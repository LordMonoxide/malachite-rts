@extends('tech.layout')

@section('body')
  @include('tech.section', ['type' => 'units', 'data' => $units])
@stop