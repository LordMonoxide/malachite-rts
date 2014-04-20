@extends('tech.layout')

@section('body')
  @include('tech.buildings.section')
    
    <section id="research">
      <h2>@lang('tech.research')</h2>
      @foreach($research as $r)
        
      @endforeach
    </section>
@stop