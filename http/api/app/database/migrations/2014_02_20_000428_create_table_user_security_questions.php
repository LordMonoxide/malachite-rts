<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableUserSecurityQuestions extends Migration {
  public function up() {
    Schema::create('user_security_questions', function($table) {
      $table->increments('id');
      $table->integer('user_id')->unsigned();
      $table->string('question', 256);
      $table->string('answer', 256);
      
      $table->timestamps();
      
      $table->foreign('user_id')
             ->references('id')
             ->on('users');
    });
  }
  
  public function down() {
    Schema::drop('user_security_questions');
  }
}