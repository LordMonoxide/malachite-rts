<!DOCTYPE html>

<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>{{ Lang::get('app.title') }} - {{ Lang::get('tech.title') }}</title>
    {{ HTML::style('assets/css/main.css') }}
    {{ HTML::style('assets/css/tech.css') }}
    {{ HTML::script('//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js') }}
  </head>
  
  <body>
    <section id="buildings">
      <h2>@lang('tech.buildings')</h2>
      
      @foreach($buildings as $b)
        <div class="pure-g">
          <div class="pure-u-1">
            <h3>{{{ $b->name }}}</h3>
          </div>
        </div>
        
        <div class="building pure-g">
          <div class="building-info pure-u-1-3">
            <h4>@lang('tech.info')</h4>
            
            @if($b->desc !== '')
              <p>{{{ $b->desc }}}</p>
            @else
              <p>@lang('tech.nodescription')</p>
            @endif
          </div>
          
          <div class="building-requirements pure-u-1-3">
            <h4>@lang('tech.requirements')</h4>
            <p>
              @if(count($b->requirements) > 0)
                @foreach($b->requirements as $r)
                  {{{ $r->requirement->name }}}
                @endforeach
              @else
                @lang('tech.norequirements')
              @endif
            </p>
          </div>
          
          <div class="building-unlocks pure-u-1-3">
            <h4>@lang('tech.unlocks')</h4>
            <p>
              @if(count($b->unlocks) > 0)
                @foreach($b->unlocks as $r)
                  {{{ $r->unlock->name }}}
                @endforeach
              @else
                @lang('tech.nounlocks')
              @endif
            </p>
          </div>
        </div>
        
        <hr />
      @endforeach
    </section>
    
    <section id="research">
      <h2>@lang('tech.research')</h2>
      @foreach($research as $r)
        
      @endforeach
    </section>
  </body>
</html>