        <div class="pure-g">
          <div class="pure-u-1">
            <p class="tech-name">
              <span class="tech-name">{{ HTML::linkAction('tech.buildings.view', Lang::get('tech.building.' . $data->name . '.name'), $data->id) }}</span> - {{ Lang::get('tech.building.' . $data->name . '.desc') }}
            </p>
          </div>
        </div>
        
        <div class="tech pure-g">
          <div class="building-info pure-u-1-4">
            <h4>@lang('tech.info')</h4>
            
            <ul>
              <li>@lang('tech.type.building.' . $data->type)</li>
            </ul>
          </div>
          
          <div class="building-requirements pure-u-1-4">
            <h4>@lang('tech.requirements')</h4>
            
            @if(count($data->requirements) > 0)
              <ul>
                @foreach($data->requirements as $r)
                  @if($r->requirement_type === 'building')
                    <li>{{ HTML::linkAction('tech.buildings.view', Lang::get('tech.building.' . $r->requirement->name . '.name'), $r->requirement->id) }}</li>
                  @elseif($r->requirement_type === 'research')
                    <li>{{ HTML::linkAction('tech.research.view',  Lang::get('tech.research.' . $r->requirement->name . '.name'), $r->requirement->id) }}</li>
                  @endif
                @endforeach
              </ul>
            @else
              <p>@lang('tech.norequirements')</p>
            @endif
          </div>
          
          <div class="building-unlocks pure-u-1-4">
            <h4>@lang('tech.unlocks')</h4>
            
            @if(count($data->unlocks) > 0)
              <ul>
                @foreach($data->unlocks as $r)
                  @if($r->requirement_type === 'building')
                    <li>{{ HTML::linkAction('tech.buildings.view', Lang::get('tech.building.' . $r->unlock->name . '.name'), $r->unlock->id) }}</li>
                  @elseif($r->requirement_type === 'research')
                    <li>{{ HTML::linkAction('tech.research.view',  Lang::get('tech.research.' . $r->unlock->name . '.name'), $r->unlock->id) }}</li>
                  @elseif($r->requirement_type === 'unit')
                    <li>{{ HTML::linkAction('tech.units.view',  Lang::get('tech.unit.' . $r->unlock->name . '.name'), $r->unlock->id) }}</li>
                  @endif
                @endforeach
              </ul>
            @else
              <p>@lang('tech.nounlocks')</p>
            @endif
          </div>
          
          <div class="building-units pure-u-1-4">
            <h4>@lang('tech.units')</h4>
            
            @if(count($data->units) > 0)
              <ul>
                @foreach($data->units as $unit)
                  <li>{{ HTML::linkAction('tech.units.view',  Lang::get('tech.unit.' . $unit->name . '.name'), $unit->id) }}</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.nounits')</p>
            @endif
          </div>
        </div>