<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableForumForums extends Migration {
  public function up() {
    Schema::create('forum_forums', function($table) {
      $table->increments('id');
      $table->integer('parent_id')->unsigned()->nullable();
      $table->string('name', 64);
      $table->timestamps();
      
      $table->foreign('parent_id')
            ->references('id')
            ->on('forum_forums');
    });
  }
  
  public function down() {
    Schema::drop('forum_forums');
  }
}