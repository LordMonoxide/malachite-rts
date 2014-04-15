<?php namespace api\storage;

use Controller;
use Response;

use News;

class NewsController extends Controller {
  public function all() {
    return Response::json(News::all(), 200);
  }
  
  public function latest() {
    return Response::json(News::latest(), 200);
  }
}