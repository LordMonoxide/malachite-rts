<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableGameSettingsBuildings extends Migration {
  public function up() {
    Schema::create('game_settings_buildings', function($table) {
      $table->increments('id');
      $table->integer('game_setting_id')->unsigned();
      $table->integer('building_id')->unsigned();
      $table->integer('count')->unsigned();
      
      $table->timestamps();
      
      $table->foreign('game_setting_id')
            ->references('id')
            ->on('game_settings');
      
      $table->foreign('building_id')
            ->references('id')
            ->on('buildings');
    });
  }
  
  public function down() {
    Schema::drop('game_settings_buildings');
  }
}