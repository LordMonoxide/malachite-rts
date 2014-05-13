<?php namespace forum\github;

use View;

use BaseController;
use Forum;
use github\Push;

class CommitsController extends BaseController {
  public function all() {
    return View::make('forum.github.commits.all')->with('forums', Forum::root()->get())->with('pushes', Push::all());
  }
}