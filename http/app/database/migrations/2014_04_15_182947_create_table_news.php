<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableNews extends Migration {
  public function up() {
    Schema::create('news', function($table) {
      $table->increments('id');
      $table->integer('topic_id')->unsigned();
      $table->timestamps();
      
      $table->foreign('topic_id')
            ->references('id')
            ->on('topics');
    });
  }
  
  public function down() {
    Schema::drop('news');
  }
}