<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableUsers extends Migration {
  public function up() {
    Schema::create('users', function($table) {
      $table->increments('id');
      $table->string('email', 254)->unique();
      $table->string('password', 60);
      $table->string('name_first', 30);
      $table->string('name_last', 30)->nullable();
      $table->boolean('logged_in')->default(false);
      $table->boolean('suspend')->default(false);
      
      $table->string('remember_token', 100)->nullable();
      
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('users');
  }
}