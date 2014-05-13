<?php namespace forum;

use Auth;
use Input;
use Redirect;
use Validator;
use View;

use BaseController;
use Forum;
use Topic;
use Post;

class ForumController extends BaseController {
  public function __construct() {
    $this->beforeFilter('auth');
  }
  
  public function index() {
    return Redirect::route('forum.forum', Forum::root()->first()->children()->first()->id);
  }
  
  public function forum($forum) {
    return View::make('forum.forum')->with('forums', Forum::root()->get())->with('forum', $forum);
  }
  
  public function viewtopic($forum, $name, $topic = null) {
    if($topic === null) { $topic = $name; }
    return View::make('forum.topic.view')->with('forums', Forum::root()->get())->with('forum', $forum)->with('topic', $topic);
  }
  
  public function replytopic($forum, $name, $topic = null) {
    if($topic === null) { $topic = $name; }
    return View::make('forum.topic.reply')->with('forums', Forum::root()->get())->with('forum', $forum)->with('topic', $topic);
  }
  
  public function submitreply($forum, $topic) {
    $validator = Validator::make(Input::all(), [
      'body'  => ['required', 'min:8']
    ]);
    
    if($validator->passes()) {
      $topic->touch();
      
      $post = new Post;
      $post->topic_id = $topic->id;
      $post->author_id = Auth::user()->id;
      $post->body = Input::get('body');
      $post->save();
      
      $info = Auth::user()->info()->first();
      $info->post_count++;
      $info->rep_pos++;
      $info->save();
      
      return Redirect::route('forum.topic.view2', [$forum->id, $topic->nameForUri, $topic->id]);
    } else {
      return Redirect::back()->withInput(Input::all())->withErrors($validator->messages());
    }
  }
  
  public function newtopic($forum) {
    return View::make('forum.topic.new')->with('forums', Forum::root()->get())->with('forum', $forum);
  }
  
  public function submittopic($forum) {
    $validator = Validator::make(Input::all(), [
      'title' => ['required', 'min:4', 'max:64', 'regex:/^[\S ]+$/'],
      'body'  => ['required', 'min:8']
    ]);
    
    if($validator->passes()) {
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
      
      $info = Auth::user()->info()->first();
      $info->post_count++;
      $info->rep_pos++;
      $info->save();
      
      return Redirect::route('forum.topic.view2', [$forum->id, $topic->nameForUri, $topic->id]);
    } else {
      return Redirect::back()->withInput(Input::all())->withErrors($validator->messages());
    }
  }
}