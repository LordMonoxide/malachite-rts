<?php namespace api\github;

use Controller;
use Response;

class HooksController extends Controller {
  public function dispatch() {
    return Response::json(null, 403);
  }
}