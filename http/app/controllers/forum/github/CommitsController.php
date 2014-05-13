<?php namespace forum\github;

use View;

use BaseController;
use github\Push;

class CommitsController extends BaseController {
  public function all() {
    return View::make('forum.github.commits.all')->with('pushes', Push::all());
  }
}