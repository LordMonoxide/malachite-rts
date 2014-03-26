<?php

use Illuminate\Database\Migrations\Migration;

class CreateTableStats extends Migration {
  public function up() {
    Schema::create('stats', function($table) {
      $table->increments('id');
      $table->string('name', 20);
      $table->string('desc', 256);
      $table->string('abbv', 3);
    });
  }
  
  public function down() {
    Schema::drop('stats');
  }
}