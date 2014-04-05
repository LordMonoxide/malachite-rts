@extends('forum.layout')

<?php function renderForum($forums, $nest = 12) {
  if($forums === null) { return; }
  
  foreach($forums as $forum) {
    echo '          <tr>
            <td style="padding-left:' . $nest . 'px">' . HTML::linkAction('forum.forum', $forum->name, $forum->id) . '</td>
          </tr>
';
    renderForum($forum->children, $nest + 12);
  }
} ?>

@section('title')
@lang('forum.title')
@stop

@section('body')
      <table class="forums pure-table pure-table-horizontal pure-table-striped">
        <tbody>
<?php renderForum($forums); ?>
        </tbody>
      </table>
@stop