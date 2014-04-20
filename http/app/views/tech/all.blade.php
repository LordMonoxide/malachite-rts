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
            <p class="building-name">
              <span class="building-name">{{{ $b->name }}}</span>
              @if($b->desc !== '')
                - {{{ $b->desc }}}
              @endif
            </p>
          </div>
        </div>
        
        <div class="building pure-g">
          <div class="building-info pure-u-1-3">
            <h4>@lang('tech.info')</h4>
            
            <ul>
              <li>@lang('tech.type.' . $b->type)</li>
            </ul>
          </div>
          
          <div class="building-requirements pure-u-1-3">
            <h4>@lang('tech.requirements')</h4>
            
            @if(count($b->requirements) > 0)
              <ul>
                @foreach($b->requirements as $r)
                  <li>{{{ $r->requirement->name }}}</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.norequirements')</p>
            @endif
          </div>
          
          <div class="building-unlocks pure-u-1-3">
            <h4>@lang('tech.unlocks')</h4>
            
            @if(count($b->unlocks) > 0)
              <ul>
                @foreach($b->unlocks as $r)
                  <li>{{{ $r->unlock->name }}}</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.nounlocks')</p>
            @endif
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