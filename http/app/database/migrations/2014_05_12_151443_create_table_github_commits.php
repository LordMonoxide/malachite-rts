<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTableGithubCommits extends Migration {
  public function up() {
    Schema::create('github_commits', function($table) {
      $table->increments('id');
      $table->integer('push_id')->unsigned();
      $table->string('author_name');
      $table->string('author_email');
      $table->string('author_username');
      $table->string('committer_name');
      $table->string('committer_email');
      $table->string('committer_username');
      $table->string('hash', 40);
      $table->boolean('distinct');
      $table->string('message');
      $table->datetime('timestamp');
      $table->string('url');
      
      $table->timestamps();
      
      $table->foreign('push_id')
            ->references('id')
            ->on('github_pushes');
    });
  }
  
  public function down() {
    Schema::drop('github_commits');
  }
}