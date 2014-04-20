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
      
      @foreach($buildings as $b)
        <p>
          <strong>{{{ $b->name }}}</strong><br />
          
          @if($b->desc !== '')
            {{{ $b->desc }}}<br /><br />
          @else
            @lang('tech.nodescription')<br /><br />
          @endif
          
          @if(count($b->requirements) > 0)
            @lang('tech.requirements')<br />
            
            @foreach($b->requirements as $r)
              {{{ $r->requirement->name }}}
            @endforeach
          @endif
        </p>
      @endforeach
    </section>
    
    <section>
      <h2>@lang('tech.research')</h2>
      @foreach($research as $r)
        
      @endforeach
    </section>
  </body>
</html>