<?php namespace forum;

use Input;
use Redirect;
use Validator;
use View;

use BaseController;
use Forum;

class SettingsController extends BaseController {
  public function __construct() {
    $this->beforeFilter('auth');
  }
  
  public function settings() {
    return Redirect::route('forum.settings.personal');
  }
  
  public function personal() {
    return View::make('forum.settings.personal');
  }
  
  public function account() {
    return View::make('forum.settings.account');
  }
  
  public function forums() {
    return View::make('forum.settings.admin.forums')->with('forums', Forum::root()->get());
  }
  
  public function newForum($parent = null) {
    return View::make('forum.settings.admin.newforum')->with('parent', $parent);
  }
  
  public function submitForum() {
    $validator = Validator::make(Input::all(), [
      'parent' => ['exists:forums,id'],
      'name'   => ['required', 'min:8']
    ]);
    
    if($validator->passes()) {
      $parent = Input::get('parent', null);
      
      $forum = new Forum;
      
      if($parent !== null) {
        $forum->parent_id = $parent;
      }
      
      $forum->name = Input::get('name');
      $forum->save();
      
      return Redirect::route('forum.settings.admin.forums');
    } else {
      return Redirect::back()->withInput(Input::all())->withErrors($validator->messages());
    }
  }
  
  public function editForum($forum) {
    dd($forum);
  }
  
  public function deleteForum($forum) {
    foreach($forum->children as $child) {
      $child->parent_id = $forum->parent_id;
      $child->save();
    }
    
    $forum->delete();
    return Redirect::route('forum.settings.admin.forums');
  }
}