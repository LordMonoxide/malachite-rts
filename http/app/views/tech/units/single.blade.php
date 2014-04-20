        <div class="pure-g">
          <div class="pure-u-1">
            <p class="tech-name">
              <span class="tech-name">{{ Lang::get('tech.unit.' . $data->name . '.name') }}</span> - {{ Lang::get('tech.unit.' . $data->name . '.desc') }}
            </p>
          </div>
        </div>
        
        <div class="tech pure-g">
          <div class="unit-info pure-u-1-2">
            <h4>@lang('tech.info')</h4>
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