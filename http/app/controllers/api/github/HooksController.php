<?php namespace api\github;

use Controller;
use Input;
use Request;
use Response;

use github\Push;
use github\Commit;
use github\CommitFile as File;

class HooksController extends Controller {
  public function dispatch() {
    switch(Request::header('x-github-event')) {
      case 'ping':   return $this->ping();
      case 'push':   return $this->push();
      case 'issues': return $this->issues();
      default:       return $this->other();
    }
  }
  
  public function ping() {
    return Response::make(null, 201);
  }
  
  public function push() {
    $data = Input::json()->all();
    
    $push = new Push;
    $push->repository_id = $data['repository']['id'];
    $push->pusher_name   = $data['pusher']['name'];
    $push->pusher_email  = $data['pusher']['email'];
    $push->ref           = $data['ref'];
    $push->before        = $data['before'];
    $push->after         = $data['after'];
    $push->created       = $data['created'];
    $push->deleted       = $data['deleted'];
    $push->forced        = $data['forced'];
    $push->compare       = $data['compare'];
    $push->save();
    
    foreach($data['commits'] as $c) {
      $commit = new Commit;
      $commit->push_id            = $push->id;
      $commit->author_name        = $c['author']['name'];
      $commit->author_email       = $c['author']['email'];
      $commit->author_username    = $c['author']['username'];
      $commit->committer_name     = $c['committer']['name'];
      $commit->committer_email    = $c['committer']['email'];
      $commit->committer_username = $c['committer']['username'];
      $commit->hash               = $c['id'];
      $commit->distinct           = $c['distinct'];
      $commit->message            = $c['message'];
      //$commit->timestamp          = $c['timestamp'];
      $commit->url                = $c['url'];
      $commit->save();
      
      foreach($c['added'] as $f) {
        $file = new File;
        $file->commit_id = $commit->id;
        $file->type = 'added';
        $file->file = $f;
        $file->save();
      }
      
      foreach($c['removed'] as $f) {
        $file = new File;
        $file->commit_id = $commit->id;
        $file->type = 'removed';
        $file->file = $f;
        $file->save();
      }
      
      foreach($c['modified'] as $f) {
        $file = new File;
        $file->commit_id = $commit->id;
        $file->type = 'modified';
        $file->file = $f;
        $file->save();
      }
    }
  }
  
  public function issues() {
    
  }
  
  public function other() {
    
  }
}