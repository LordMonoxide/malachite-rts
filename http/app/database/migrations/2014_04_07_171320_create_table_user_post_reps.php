<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableUserPostReps extends Migration {
  public function up() {
    Schema::create('user_post_reps', function($table) {
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
            ->on('posts');
    });
  }
  
  public function down() {
    Schema::drop('user_post_reps');
  }
}