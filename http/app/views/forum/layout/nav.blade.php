<?php function renderChildren($forums, $current, $nest = 12) {
  if($forums === null) { return; }
  foreach($forums as $f) {
    if($current != null) {
      echo '<li style="padding-left:' . $nest . 'px;' . ($current->id === $f->id ? 'font-weight:bold' : '') . '">' . HTML::linkRoute('forum.forum', $f->name, $f->id) . '</li>';
    } else {
      echo '<li style="padding-left:' . $nest . 'px;">' . HTML::linkRoute('forum.forum', $f->name, $f->id) . '</li>';
    }
    
    renderChildren($f->children, $current, $nest + 12);
  }
} ?>

<div class="nav pure-menu pure-menu-open">
  <ul>
    @foreach($forums as $f)
      <li class="pure-menu-heading">{{ $f->name }}</li>
      <?php renderChildren($f->children, $forum); ?>
    @endforeach
    
    <li class="pure-menu-heading">@lang('forum.github.title')</li>
    <li>{{ HTML::linkAction('forum.github.commits.all', Lang::get('forum.github.commits.title'), null, ['style' => Route::currentRouteName() === 'forum.github.commits.all' ? 'font-weight:bold' : '']) }}</li>
    <li>{{ HTML::linkAction('forum.github.issues.all',  Lang::get('forum.github.issues.title'),  null, ['style' => Route::currentRouteName() === 'forum.github.issues.all'  ? 'font-weight:bold' : '']) }}</li>
  </ul>
</div>