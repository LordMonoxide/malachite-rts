<?php function renderChildren($forums, $current, $nest = 8) {
  if($forums === null) { return; }
  foreach($forums as $f) {
    echo '<li style="padding-left:' . $nest . 'px;' . ($current->id === $f->id ? 'font-weight:bold' : '') . '">' . HTML::linkRoute('forum.forum', $f->name, $f->id) . '</li>';
    renderChildren($f->children, $current, $nest + 8);
  }
} ?>

<div class="nav pure-menu pure-menu-open">
  <ul>
    @foreach($forums as $f)
      <li class="pure-menu-heading">{{ $f->name }}</li>
      <?php renderChildren($f->children, $forum); ?>
    @endforeach
  </ul>
</div>