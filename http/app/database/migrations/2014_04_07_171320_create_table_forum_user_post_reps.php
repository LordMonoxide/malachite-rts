<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableForumUserPostReps extends Migration {
  public function up() {
    Schema::create('forum_user_post_reps', function($table) {
      $table->increments('id');
      $table->integer('user_id')->unsigned();
      $table->integer('post_id')->unsigned();
      $table->integer('rep');
      $table->timestamps();
      
      $table->foreign('user_id')
            ->references('id')
            ->on('users');
      
      $table->foreign('post_id')
            ->references('id')
            ->on('forum_posts');
    });
  }
  
  public function down() {
    Schema::drop('forum_user_post_reps');
  }
}