        <div class="pure-g">
          <div class="pure-u-1">
            <p class="building-name">
              <span class="building-name">{{ Lang::get('tech.building.' . $building->name . '.name') }} - {{ Lang::get('tech.building.' . $building->name . '.desc') }}</span>
            </p>
          </div>
        </div>
        
        <div class="building pure-g">
          <div class="building-info pure-u-1-4">
            <h4>@lang('tech.info')</h4>
            
            <ul>
              <li>@lang('tech.type.' . $building->type)</li>
            </ul>
          </div>
          
          <div class="building-requirements pure-u-1-4">
            <h4>@lang('tech.requirements')</h4>
            
            @if(count($building->requirements) > 0)
              <ul>
                @foreach($building->requirements as $r)
                  <li>{{{ $r->requirement->name }}}</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.norequirements')</p>
            @endif
          </div>
          
          <div class="building-unlocks pure-u-1-4">
            <h4>@lang('tech.unlocks')</h4>
            
            @if(count($building->unlocks) > 0)
              <ul>
                @foreach($building->unlocks as $r)
                  <li>{{{ $r->unlock->name }}}</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.nounlocks')</p>
            @endif
          </div>
          
          <div class="building-units pure-u-1-4">
            <h4>@lang('tech.units')</h4>
            
            @if(count($building->units) > 0)
              <ul>
                @foreach($building->units as $unit)
                  <li>@lang('tech.unit.' . $unit->name . '.name')</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.nounits')</p>
            @endif
          </div>
        </div>