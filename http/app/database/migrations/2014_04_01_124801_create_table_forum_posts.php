<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableForumPosts extends Migration {
	public function up() {
    Schema::create('forum_posts', function($table) {
      $table->increments('id');
      $table->integer('topic_id')->unsigned();
      $table->integer('author_id')->unsigned();
      $table->longText('body');
      $table->int('rep_pos')->unsigned()->default(0);
      $table->int('rep_neg')->unsigned()->default(0);
      $table->timestamps();
      
      $table->foreign('topic_id')
            ->references('id')
            ->on('forum_topics');
      
      $table->foreign('author_id')
            ->references('id')
            ->on('users');
    });
  }
  
  public function down() {
    Schema::drop('forum_posts');
  }
}