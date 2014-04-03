<?php

return [
  'topic.link' => HTML::linkAction('forum.view', ':title', [':category', ':topic']) . '<br /><cite class="post-citation">by :user on :date</cite>'
];