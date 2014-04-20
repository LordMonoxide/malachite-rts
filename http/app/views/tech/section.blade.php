    <section id="{{ $type }}" class="tech-section">
      <h2>@lang('tech.' . $type)</h2>
      
      @foreach($data as $d)
        @include('tech.' . $type . '.single', ['data' => $d])
        
        <hr />
      @endforeach
    </section>