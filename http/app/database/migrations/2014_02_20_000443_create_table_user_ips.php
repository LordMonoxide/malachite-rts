<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableUserIps extends Migration {
  public function up() {
    Schema::create('user_ips', function($table) {
      $table->increments('id');
      $table->integer('user_id')->unsigned();
      $table->integer('ip');
      $table->boolean('authorised')->default(false);
      
      $table->timestamps();
      
      $table->foreign('user_id')
             ->references('id')
             ->on('users');
    });
  }
  
  public function down() {
    Schema::drop('user_ips');
  }
}