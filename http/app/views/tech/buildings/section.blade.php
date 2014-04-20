    <section id="buildings">
      <h2>@lang('tech.buildings')</h2>
      
      @foreach($buildings as $b)
        <div class="pure-g">
          <div class="pure-u-1">
            <p class="building-name">
              <span class="building-name">{{ Lang::get('tech.building.' . $b->name . '.name') }} - {{ Lang::get('tech.building.' . $b->name . '.desc') }}</span>
            </p>
          </div>
        </div>
        
        <div class="building pure-g">
          <div class="building-info pure-u-1-4">
            <h4>@lang('tech.info')</h4>
            
            <ul>
              <li>@lang('tech.type.' . $b->type)</li>
            </ul>
          </div>
          
          <div class="building-requirements pure-u-1-4">
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
          
          <div class="building-unlocks pure-u-1-4">
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
          
          <div class="building-units pure-u-1-4">
            <h4>@lang('tech.units')</h4>
            
            @if(count($b->units) > 0)
              <ul>
                @foreach($b->units as $unit)
                  <li>@lang('tech.unit.' . $unit->name . '.name')</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.nounits')</p>
            @endif
          </div>
        </div>
        
        <hr />
      @endforeach
    </section>