<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableGithubPushes extends Migration {
  public function up() {
    Schema::create('github_pushes', function($table) {
      $table->increments('id');
      $table->integer('repository_id')->unsigned();
      $table->string('pusher_name');
      $table->string('pusher_email');
      $table->string('ref');
      $table->string('before', 40);
      $table->string('after', 40);
      $table->boolean('created');
      $table->boolean('deleted');
      $table->boolean('forced');
      $table->string('compare');
      
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('github_pushes');
  }
}