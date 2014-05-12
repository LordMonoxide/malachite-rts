<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableGithubCommitFiles extends Migration {
  public function up() {
    Schema::create('github_commit_files', function($table) {
      $table->increments('id');
      $table->integer('commit_id')->unsigned();
      $table->enum('type', ['added', 'removed', 'modified']);
      $table->string('file');
      
      $table->timestamps();
      
      $table->foreign('commit_id')
            ->references('id')
            ->on('github_commits');
    });
  }
  
  public function down() {
    Schema::drop('github_commit_files');
  }
}