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
  'topic.view.link' => HTML::linkAction('forum.topic.view2', ':title', [':forum', ':name', ':topic']) . '<br /><cite class="post-citation">by :user on :date</cite>',
  'topic.newestpost' => '<cite class="post-citation">by :user on :date</cite>',
  'newpost.title' => 'New Post',
  'newpost.post.title' => 'Title',
  'newpost.post.body' => 'Body',
  'newpost.post.submit' => 'Create',
  'topic.reply' => 'Reply',
  'topic.reply.body' => 'Body',
  'topic.reply.submit' => 'Reply',
  'topic.rep' => 'Was this post helpful?',
  'topic.rep.pos' => '+:rep',
  'topic.rep.neg' => ':rep'
];