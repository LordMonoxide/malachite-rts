<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableForumCategories extends Migration {
  public function up() {
    Schema::create('forum_categories', function($table) {
      $table->increments('id');
      $table->string('name', 64);
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('forum_categories');
  }
}