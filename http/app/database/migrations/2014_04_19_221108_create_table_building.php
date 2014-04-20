<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableBuilding extends Migration {
  public function up() {
    Schema::create('buildings', function($table) {
      $table->increments('id');
      $table->string('name', 64);
      $table->enum('type', ['base', 'foodstore', 'woodstore', 'metalstore', 'housing']);
      $table->timestamps();
    });
  }
  
  public function down() {
    Schema::drop('buildings');
  }
}