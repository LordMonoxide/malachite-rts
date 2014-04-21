@extends('tech.layout')

@section('body')
    <section id="{{ $type }}" class="tech-section">
      @include('tech.' . $type . '.single', ['data' => $data])
    </section>
@stop