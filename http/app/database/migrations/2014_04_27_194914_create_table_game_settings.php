<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableGameSettings extends Migration {
  public function up() {
    Schema::create('game_settings', function($table) {
      $table->increments('id');
      $table->string('name');
      $table->integer('units')->unsigned();
      
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('game_settings');
  }
}