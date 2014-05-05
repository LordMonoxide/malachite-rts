@extends('forum.settings.layout')

<?php function renderForum($fs, $nest = 12) {
  if($fs === null) { return; }
  
  foreach($fs as $f) {
    echo '          <tr>
            <td class="forum-title" style="padding-left:' . $nest . 'px">' .
              HTML::linkAction('forum.forum', $f->name, $f->id) .
              
              '<a href="' . URL::action('forum.settings.admin.forums.new.sub', [$f->id]) . '">' .
                HTML::image('assets/img/forum/new.png', Lang::get('forum.settings.admin.forums.new.title'), ['class' => 'icon']) .
              '</a>' .
              '<a href="' . URL::action('forum.settings.admin.forums.edit', [$f->id]) . '">' .
                HTML::image('assets/img/forum/settings.png', Lang::get('forum.settings.admin.forums.edit'), ['class' => 'icon']) .
              '</a>' .
              '<a href="' . URL::action('forum.settings.admin.forums.delete', [$f->id]) . '">' .
                HTML::image('assets/img/forum/delete.png', Lang::get('forum.settings.admin.forums.delete'), ['class' => 'icon']) .
              '</a>' .
            '</td>
          </tr>
';
    renderForum($f->children, $nest + 12);
  }
} ?>

@section('settings')
  <table class="forums pure-table pure-table-horizontal pure-table-striped">
    <thead>
      <tr>
        <th>@lang('forum.name')</th>
      </tr>
    </thead>
    
    <tbody>
<?php renderForum($forums); ?>
      
      <tr>
        <td class="forum-title">{{ Lang::get('forum.settings.admin.forums.new.root') }}<a href="{{ URL::action('forum.settings.admin.forums.new') }}">{{ HTML::image('assets/img/forum/new.png', Lang::get('forum.settings.admin.forums.new.title'), ['class' => 'icon']) }}</a></td>
      </tr>
    </tbody>
  </table>
@stop