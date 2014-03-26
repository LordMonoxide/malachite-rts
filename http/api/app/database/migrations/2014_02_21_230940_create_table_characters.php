<?php

use Illuminate\Database\Migrations\Migration;

class CreateTableCharacters extends Migration {
  public function up() {
    Schema::create('characters', function($table) {
      $table->increments('id');
      $table->integer('user_id')->unsigned();
      $table->integer('auth_id')->unsigned()->nullable();
      
      $table->string('name', 20);
      $table->integer('race_id')->unsigned();
      $table->enum('sex', ['male', 'female']);
      
      $table->integer('hp')->unsigned()->default(0);
      $table->integer('mp')->unsigned()->default(0);
      
      $table->timestamps();
      
      $table->foreign('user_id')
             ->references('id')
             ->on('users');
      
      $table->foreign('race_id')
             ->references('id')
             ->on('races');
    });
  }
  
  public function down() {
    Schema::drop('characters');
  }
}