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
    $topic = false;
    
    foreach($paths as $path) {
      if($path === 'new') {
        return View::make('forum.topic.new')->with('category', $forums[0]->category)->with('forums', $forums)->with('forum', $lastForum);
      }
      
      if($path === 'topic') {
        $topic = true;
        continue;
      }
      
      if(preg_match($pattern, $path) === 0) {
        App::abort(404);
      }
      
      $forum = explode('-', $path, 1)[0];
      
      if($topic) {
        $topic = Topic::where('id', '=', $forum)->where('forum_id', '=', $lastForum->id)->first();
        
        if($topic === null) {
          App::abort(404);
        }
        
        return View::make('forum.topic.view')->with('category', $forums[0]->category)->with('forums', $forums)->with('forum', $lastForum)->with('topic', $topic);
      }
      
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
    
    return View::make('forum.view')->with('category', $forums[0]->category)->with('forums', $forums)->with('forum', $lastForum);
  }
  
  public function category($category) {
    return View::make('forum.category')->with('category', $category);
  }
}