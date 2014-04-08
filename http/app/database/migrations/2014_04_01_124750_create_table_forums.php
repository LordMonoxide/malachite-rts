<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableForums extends Migration {
  public function up() {
    Schema::create('forums', function($table) {
      $table->increments('id');
      $table->integer('parent_id')->unsigned()->nullable();
      $table->string('name', 64);
      $table->timestamps();
      
      $table->foreign('parent_id')
            ->references('id')
            ->on('forums');
    });
  }
  
  public function down() {
    Schema::drop('forums');
  }
}