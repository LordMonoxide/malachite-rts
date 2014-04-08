<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableUserInfos extends Migration {
	public function up() {
    Schema::create('user_infos', function($table) {
      $table->integer('user_id')->unsigned()->unique();
      $table->string('title')->nullable();
      $table->string('location')->nullable();
      $table->integer('post_count')->unsigned()->default(0);
      $table->integer('rep_pos')->unsigned()->default(0);
      $table->integer('rep_neg')->unsigned()->default(0);
      
      $table->foreign('user_id')
            ->references('id')
            ->on('users');
    });
  }
  
  public function down() {
    Schema::drop('user_infos');
  }
}