<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableRequirement extends Migration {
  public function up() {
    Schema::create('requirements', function($table) {
      $table->increments('id');
      $table->integer('unlock_id')->unsigned();
      $table->enum('unlock_type', ['building', 'research', 'unit']);
      $table->integer('requirement_id')->unsigned();
      $table->enum('requirement_type', ['building', 'research']);
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('requirements');
  }
}