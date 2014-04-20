<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('tech.title') }}</title>
    {{ HTML::style('assets/css/main.css') }}
    {{ HTML::script('//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js') }}
  </head>
  
  <body>
    <section>
      <h2>@lang('tech.buildings')</h2>
      <hr />
      
      @foreach($buildings as $b)
        <h3>{{{ $b->name }}}</h3>
        
        @if($b->desc !== '')
          <p>{{{ $b->desc }}}</p>
        @else
          <p>@lang('tech.nodescription')</p>
        @endif
        
        <p>
        @if(count($b->requirements) > 0)
          <strong>@lang('tech.requirements')</strong><br />
          
          @foreach($b->requirements as $r)
            {{{ $r->requirement->name }}}
          @endforeach
        @endif
        </p>
        
        <hr />
      @endforeach
    </section>
    
    <section>
      <h2>@lang('tech.research')</h2>
      @foreach($research as $r)
        
      @endforeach
    </section>
  </body>
</html>