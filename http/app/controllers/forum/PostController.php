<?php namespace forum;

use Auth;
use Redirect;

use BaseController;
use UserPostRep;

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
    
    $info = $post->author->info;
    $info->rep_pos += 10;
    $info->save();
    
    $post->rep_pos += 1;
    $post->save();
    
    return Redirect::back();
  }
  
  public function repneg($post) {
    $rep = new UserPostRep;
    $rep->user_id = Auth::user()->id;
    $rep->post_id = $post->id;
    $rep->rep = -1;
    $rep->save();
    
    $info = $post->author->info;
    $info->rep_neg += 10;
    $info->save();
    
    $post->rep_neg += 1;
    $post->save();
    
    return Redirect::back();
  }
}