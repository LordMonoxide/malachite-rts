<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableResearch extends Migration {
  public function up() {
    Schema::create('research', function($table) {
      $table->increments('id');
      $table->integer('building_id')->unsigned();
      $table->string('name', 64);
      
      $table->timestamps();
      
      $table->foreign('building_id')
            ->references('id')
            ->on('buildings');
    });
  }
  
  public function down() {
    Schema::drop('research');
  }
}