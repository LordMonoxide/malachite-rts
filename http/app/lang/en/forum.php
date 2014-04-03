<?php

return [
  'name' => 'Forum',
  'title' => 'Title',
  'index' => 'Index',
  'replies' => 'Replies',
  'reply' => 'Reply',
  'noposts' => 'There are no posts in this forum',
  'newestpost' => 'Newest Post',
  'new' => 'New Post',
  'topic.link' => HTML::linkAction('forum.view', ':title', [':category', ':topic']) . '<br /><cite class="post-citation">by :user on :date</cite>',
  'topic.newestpost' => '<cite class="post-citation">by :user on :date</cite>',
  'newpost.title' => 'New Post',
  'newpost.post.title' => 'Title',
  'newpost.post.body' => 'Body',
  'newpost.post.submit' => 'Create',
  'topic.reply' => 'Reply',
  'topic.reply.body' => 'Body',
  'topic.reply.submit' => 'Reply'
];