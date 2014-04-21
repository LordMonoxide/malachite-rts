<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableUnit extends Migration {
  public function up() {
    Schema::create('units', function($table) {
      $table->increments('id');
      $table->integer('building_id')->unsigned();
      $table->string('name', 64);
      $table->enum('type', ['villager']);
      
      $table->timestamps();
      
      $table->foreign('building_id')
            ->references('id')
            ->on('buildings');
    });
  }
  
  public function down() {
    Schema::drop('units');
  }
}