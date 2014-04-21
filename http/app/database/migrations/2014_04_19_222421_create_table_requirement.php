<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableRequirement extends Migration {
  public function up() {
    Schema::create('requirements', function($table) {
      $table->increments('id');
      $table->integer('unlock_id')->unsigned();
      $table->enum('unlock_type', ['building', 'research', 'unit']);
      $table->integer('research_id')->unsigned();
      
      $table->timestamps();
      
      $table->foreign('research_id')
            ->references('id')
            ->on('research');
    });
  }
  
  public function down() {
    Schema::drop('requirements');
  }
}