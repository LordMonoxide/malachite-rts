<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableResearch extends Migration {
  public function up() {
    Schema::create('researches', function($table) {
      $table->increments('id');
      $table->string('name', 64);
      $table->string('desc');
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('researches');
  }
}