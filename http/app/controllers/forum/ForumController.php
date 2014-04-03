<?php namespace forum;

use App;
use Auth;
use Input;
use Redirect;
use Session;
use Validator;
use View;

use BaseController;
use ForumCategory as Category;
use ForumForum as Forum;
use ForumTopic as Topic;
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
      
      Session::flash('forum', $lastForum->id);
    }
    
    return View::make('forum.view')->with('category', $forums[0]->category)->with('forums', $forums)->with('forum', $lastForum);
  }
  
  public function category($category) {
    return View::make('forum.category')->with('category', $category);
  }
  
  public function newTopic() {
    $validator = Validator::make(Input::all(), [
      'title' => ['required', 'min:4', 'max:64', 'regex:/^[\S ]+$/'],
      'body'  => ['required', 'min:8']
    ]);
    
    if($validator->passes()) {
      $forum = Forum::find(Session::get('forum'));
      
      $topic = new Topic;
      $topic->forum_id = $forum->id;
      $topic->creator_id = Auth::user()->id;
      $topic->title = Input::get('title');
      $topic->save();
      
      $post = new Post;
      $post->topic_id = $topic->id;
      $post->author_id = Auth::user()->id;
      $post->body = Input::get('body');
      $post->save();
      
      return Redirect::route('forum.view', [$forum->category->id, $topic->path]);
    } else {
      return Redirect::back()->withInput(Input::all())->withErrors($validator->messages());
    }
  }
}