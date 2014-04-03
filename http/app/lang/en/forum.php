<?php

return [
  'name' => 'Forum',
  'title' => 'Title',
  'replies' => 'Replies',
  'newestpost' => 'Newest Post',
  'new' => 'New Post',
  'topic.link' => HTML::linkAction('forum.view', ':title', [':category', ':topic']) . '<br /><cite class="post-citation">by :user on :date</cite>',
  'topic.newestpost' => '<cite class="post-citation">by :user on :date</cite>'
];