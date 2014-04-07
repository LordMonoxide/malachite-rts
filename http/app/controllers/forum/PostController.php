<?php namespace forum;

use Auth;
use Redirect;

use BaseController;
use ForumUserPostRep as UserPostRep;

class PostController extends BaseController {
  public function __construct() {
    $this->beforeFilter('auth');
  }
  
  public function reppos($post) {
    $rep = new UserPostRep;
    $rep->user_id = Auth::user()->id;
    $rep->post_id = $post->id;
    $rep->rep = 1;
    $rep->save();
    
    $info = $post->author->forumInfo;
    $info->rep_pos += 10;
    $info->save();
    
    return Redirect::back();
  }
  
  public function repneg($post) {
    $rep = new UserPostRep;
    $rep->user_id = Auth::user()->id;
    $rep->post_id = $post->id;
    $rep->rep = -1;
    $rep->save();
    
    $info = $post->author->forumInfo;
    $info->rep_neg += 10;
    $info->save();
    
    return Redirect::back();
  }
}