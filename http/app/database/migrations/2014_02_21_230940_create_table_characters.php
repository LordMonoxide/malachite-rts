<?php

use Illuminate\Database\Migrations\Migration;

class CreateTableCharacters extends Migration {
  public function up() {
    Schema::create('characters', function($table) {
      $table->increments('id');
      $table->integer('user_id')->unsigned();
      $table->integer('auth_id')->unsigned()->nullable();
      
      $table->string('name', 20);
      $table->enum('sex', ['male', 'female']);
      
      $table->integer('lvl')->unsigned()->default(1);
      $table->integer('exp')->unsigned()->default(0);
      
      $table->integer('hp')->unsigned()->default(0);
      $table->integer('mp')->unsigned()->default(0);
      
      $table->integer('str')->unsigned()->default(0);
      $table->integer('dex')->unsigned()->default(0);
      $table->integer('int')->unsigned()->default(0);
      
      $table->timestamps();
      
      $table->foreign('user_id')
             ->references('id')
             ->on('users');
    });
  }
  
  public function down() {
    Schema::drop('characters');
  }
}