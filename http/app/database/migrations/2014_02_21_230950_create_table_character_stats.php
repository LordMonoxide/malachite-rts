<?php

use Illuminate\Database\Migrations\Migration;

class CreateTableCharacterStats extends Migration {
  public function up() {
    Schema::create('character_stats', function($table) {
      $table->increments('id');
      $table->integer('user_id')->unsigned();
      $table->integer('stat_id')->unsigned();
      $table->integer('lvl')->unsigned()->default(0);
      $table->integer('exp')->unsigned()->default(0);
      $table->timestamps();
      
      $table->foreign('user_id')
             ->references('id')
             ->on('users');
      
      $table->foreign('stat_id')
             ->references('id')
             ->on('stats');
    });
  }
  
  public function down() {
    Schema::drop('stats');
  }
}