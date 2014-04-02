<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableTopics extends Migration {
  public function up() {
    Schema::create('forum_topics', function($table) {
      $table->increments('id');
      $table->integer('forum_id')->unsigned();
      $table->integer('creator_id')->unsigned();
      $table->boolean('pinned')->default(false);
      $table->string('title', 64);
      $table->timestamps();
      
      $table->foreign('forum_id')
            ->references('id')
            ->on('forum_forums');
      
      $table->foreign('creator_id')
            ->references('id')
            ->on('users');
    });
  }
  
  public function down() {
    Schema::drop('forum_topics');
  }
}