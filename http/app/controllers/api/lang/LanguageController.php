<?php namespace api\lang;

use Controller;
use Lang;
use Response;

class LanguageController extends Controller {
  public function app() {
    return Response::json(Lang::get('app'), 200);
  }
  
  public function menu() {
    return Response::json(Lang::get('menu'), 200);
  }
}