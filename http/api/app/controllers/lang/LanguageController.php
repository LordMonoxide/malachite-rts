<?php namespace lang;

use Controller;
use Response;

class LangController extends Controller {
  public function menu() {
    return Response::json(Lang::get('menu'), 200);
  }
}