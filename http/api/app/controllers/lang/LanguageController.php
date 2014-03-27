<?php namespace lang;

use Controller;
use Lang;
use Response;

class LanguageController extends Controller {
  public function menu() {
    return Response::json(Lang::get('menu'), 200);
  }
}