@extends('forum.layout')

<?php function renderForum($forums, $nest = 12) {
  if($forums === null) { return; }
  
  foreach($forums as $f) {
    echo '          <tr>
            <td style="padding-left:' . $nest . 'px">' . HTML::linkAction('forum.forum', $f->name, $f->id) . '</td>
          </tr>
';
    renderForum($f->children, $nest + 12);
  }
} ?>

@section('title')
@lang('forum.title')
@stop

@section('body')
  @foreach($forums as $f)
      <table class="forums pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>{{ HTML::linkAction('forum.forum', $f->name, $f->id) }}</th>
          </tr>
        </thead>
        
        <tbody>
          <?php renderForum($f->children); ?>
        </tbody>
      </table>
  @endforeach
@stop