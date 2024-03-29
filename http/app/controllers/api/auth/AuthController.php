<?php namespace api\auth;

use Auth;
use Controller;
use Hash;
use Input;
use Request;
use Response;
use Validator;

use User;
use UserInfo;
use UserSecurityQuestion;

class AuthController extends Controller {
  public function __construct() {
    $this->beforeFilter('user.security',  ['only'   => ['check', 'logout', 'security']]);
    $this->beforeFilter('auth.401',       ['only'   => ['unlock']]);
    $this->beforeFilter('nauth.409',      ['except' => ['check', 'logout', 'security', 'unlock']]);
  }
  
  public function check() {
    return Response::json(null, 204);
  }
  
  public function register() {
    $validator = Validator::make(Input::all(), [
      'email'      => ['required', 'email', 'unique:users,email'],
      'password'   => ['required', 'min:8', 'max:256', 'confirmed'],
      'name_first' => ['required', 'min:2', 'max:30'],
      'name_last'  => ['min:2', 'max:30'],
      'remember'   => ['in:yes,no,on,off,1,0'],
      'question1'  => ['required', 'min:1', 'max:255'],
      'question2'  => ['required', 'min:1', 'max:255'],
      'question3'  => ['required', 'min:1', 'max:255'],
      'answer1'    => ['required', 'min:1', 'max:255'],
      'answer2'    => ['required', 'min:1', 'max:255'],
      'answer3'    => ['required', 'min:1', 'max:255']
    ]);
    
    if($validator->passes()) {
      $user = new User;
      $user->email      = Input::get('email');
      $user->password   = Hash::make(Input::get('password'));
      $user->name_first = Input::get('name_first');
      $user->name_last  = Input::get('name_last');
      $user->save();
      
      $sec = new UserSecurityQuestion;
      $sec->user_id  = $user->id;
      $sec->question = Input::get('question1');
      $sec->answer   = Input::get('answer1');
      $sec->save();
      
      $sec = new UserSecurityQuestion;
      $sec->user_id  = $user->id;
      $sec->question = Input::get('question2');
      $sec->answer   = Input::get('answer2');
      $sec->save();
      
      $sec = new UserSecurityQuestion;
      $sec->user_id  = $user->id;
      $sec->question = Input::get('question3');
      $sec->answer   = Input::get('answer3');
      $sec->save();
      
      $userInfo = new UserInfo;
      $userInfo->user_id = $user->id;
      $userInfo->save();
      
      $remember = Input::get('remember', false);
      if($remember === 'yes' || $remember === 'on')  { $remember = true; }
      if($remember === 'no'  || $remember === 'off') { $remember = false; }
      
      Auth::login($user, $remember);
      
      return Response::json(['id' => Auth::user()->id], 200);
    } else {
      return Response::json($validator->messages(), 409);
    }
  }
  
  public function login() {
    $validator = Validator::make(Input::all(), [
      'email'    => ['required', 'email', 'exists:users,email'],
      'password' => ['required', 'min:8', 'max:256'],
      'remember' => ['in:true,false,yes,no,on,off,1,0']
    ]);
    
    if($validator->passes()) {
      $remember = Input::get('remember', false);
      if($remember === 'yes' || $remember === 'on')  { $remember = true; }
      if($remember === 'no'  || $remember === 'off') { $remember = false; }
      
      if(!Auth::attempt(Input::only(['email', 'password']), $remember)) {
        return Response::json(['password' => ['Invalid password']], 409);
      }
      
      $ip = Auth::user()->ips()->where('ip', '=', ip2long(Request::getClientIp()))->first();
      if(!$ip->authorised) {
        Auth::user()->suspend = true;
        Auth::user()->save();
        
        return Response::json([
          'error' => 'security',
          'show'  => 'security'
        ], 401);
      }
      
      return Response::json(['id' => Auth::user()->id], 200);
    } else {
      return Response::json($validator->messages(), 409);
    }
  }
  
  public function security() {
    return Response::json(Auth::user()->securityQuestions, 200);
  }
  
  public function unlock() {
    $i = 1;
    foreach(Auth::user()->securityQuestions as $s) {
      $rules['answer' . $i++] = ['required', 'in:' . $s->answer];
    }
    
    $validator = Validator::make(Input::all(), $rules);
    if($validator->passes()) {
      $ip = Auth::user()->ips()->where('ip', '=', ip2long(Request::getClientIp()))->first();
      $ip->authorised = true;
      $ip->save();
      
      Auth::user()->suspend = false;
      Auth::user()->save();
      return Response::json(null, 204);
    } else {
      return Response::json($validator->messages(), 409);
    }
  }
  
  public function logout() {
    Auth::logout();
    return Response::json(null, 204);
  }
}