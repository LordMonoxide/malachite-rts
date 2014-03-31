<?php

use Illuminate\Database\Migrations\Migration;

class CreateTableRaces extends Migration {
  public function up() {
    Schema::create('races', function($table) {
      $table->increments('id');
      $table->string('name', 20);
    });
  }
  
  public function down() {
    Schema::drop('races');
  }
}