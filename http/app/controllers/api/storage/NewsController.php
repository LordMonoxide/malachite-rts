<?php namespace api\storage;

use Controller;
use Response;

use News;

class NewsController extends Controller {
  public function all() {
    $posts = [];
    News::all()->each(function($news) use(&$posts) {
      $posts[] = ['id' => $news->id, 'title' => $news->topic->title, 'body' => $news->topic->posts->first()->body];
    });
    
    return Response::json($posts, 200);
  }
  
  public function latest() {
    $news = News::latest()->first();
    
    if($news !== null) {
      $post = ['id' => $news->id, 'title' => $news->topic->title, 'body' => $news->topic->posts->first()->body];
      return Response::json($post, 200);
    } else {
      return Response::json(null, 204);
    }
  }
}