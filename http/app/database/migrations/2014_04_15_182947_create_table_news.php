<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableNews extends Migration {
  public function up() {
    Schema::create('news', function($table) {
      $table->increments('id');
      $table->integer('post_id')->unsigned();
      $table->timestamps();
      
      $table->foreign('post_id')
            ->references('id')
            ->on('posts');
    });
  }
  
  public function down() {
    Schema::drop('news');
  }
}