        <div class="pure-g">
          <div class="pure-u-1">
            <p class="tech-name">
              <span class="tech-name">{{ HTML::linkAction('tech.units.view', Lang::get('tech.unit.' . $data->name . '.name'), $data->id) }}</span> - {{ Lang::get('tech.unit.' . $data->name . '.desc') }}
            </p>
          </div>
        </div>
        
        <div class="tech pure-g">
          <div class="unit-info pure-u-1-2">
            <h4>@lang('tech.info')</h4>
            <ul>
              <li>{{ Lang::get('tech.unit.trainedin') }} {{ HTML::linkAction('tech.buildings.view', Lang::get('tech.building.' . $data->building->name . '.name'), $data->building->id) }}</li>
            </ul>
          </div>
          
          <div class="unit-requirements pure-u-1-2">
            <h4>@lang('tech.requirements')</h4>
            
            @if(count($data->requirements) > 0)
              <ul>
                @foreach($data->requirements as $r)
                  <li>{{{ $r->requirement->name }}}</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.norequirements')</p>
            @endif
          </div>
        </div>