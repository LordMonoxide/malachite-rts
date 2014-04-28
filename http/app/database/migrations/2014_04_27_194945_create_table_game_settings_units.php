<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableGameSettingsUnits extends Migration {
  public function up() {
    Schema::create('game_settings_units', function($table) {
      $table->increments('id');
      $table->integer('game_setting_id')->unsigned();
      $table->integer('unit_id')->unsigned();
      $table->integer('count')->unsigned();
      
      $table->timestamps();
      
      $table->foreign('game_setting_id')
            ->references('id')
            ->on('game_settings');
      
      $table->foreign('unit_id')
            ->references('id')
            ->on('units');
    });
  }
  
  public function down() {
    Schema::drop('game_settings_units');
  }
}