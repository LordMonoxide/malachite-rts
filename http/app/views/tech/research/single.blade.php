        <div class="pure-g">
          <div class="pure-u-1">
            <p class="tech-name">
              <span class="tech-name">{{ Lang::get('tech.research.' . $data->name . '.name') }}</span> - {{ Lang::get('tech.research.' . $data->name . '.desc') }}
            </p>
          </div>
        </div>
        
        <div class="tech pure-g">
          <div class="research-info pure-u-1-3">
            <h4>@lang('tech.info')</h4>
            <ul>
              <li>{{ Lang::get('tech.unit.researchedin') }} {{ HTML::linkAction('tech.buildings.view', Lang::get('tech.building.' . $data->building->name . '.name'), $data->building->id) }}</li>
            </ul>
          </div>
          
          <div class="research-requirements pure-u-1-3">
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
          
          <div class="research-unlocks pure-u-1-3">
            <h4>@lang('tech.unlocks')</h4>
            
            @if(count($data->unlocks) > 0)
              <ul>
                @foreach($data->unlocks as $r)
                  <li>{{{ $r->unlock->name }}}</li>
                @endforeach
              </ul>
            @else
              <p>@lang('tech.nounlocks')</p>
            @endif
          </div>
        </div>