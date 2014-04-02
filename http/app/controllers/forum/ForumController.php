<?php namespace forum;

use App;
use View;

use BaseController;
use ForumCategory as Category;
use ForumForum as Forum;
use ForumPost as Post;

class ForumController extends BaseController {
  public function __construct() {
    $this->beforeFilter('auth');
  }
  
  public function index() {
    return View::make('forum.index')->with('categories', Category::all());
  }
  
  public function view($category, $path) {
    $paths = explode('/', $path);
    $pattern = '/(?:[0-9]+(?:-[-\w]+)?)+/';
    $forums = [];
    $lastForum = null;
    
    foreach($paths as $path) {
      if(preg_match($pattern, $path) === 0) {
        App::abort(404);
      }
      
      $forum = explode('-', $path, 1)[0];
      
      if($lastForum === null) {
        $forum = Forum::where('id', '=', $forum)->where('category_id', '=', $category->id)->first();
      } else {
        $forum = Forum::where('id', '=', $forum)->where('parent_id', '=', $lastForum->id)->first();
      }
      
      if($forum === null) {
        App::abort(404);
      }
      
      $forums[] = $forum;
      $lastForum = $forum;
    }
  }
  
  public function category($category) {
    return View::make('forum.category')->with('category', $category);
  }
  
  /*public function forum($forum) {
    return View::make('forum.forum')->with('forum', $forum);
  }*/
}