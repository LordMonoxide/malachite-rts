@extends('forum.layout')

@section('title')
  @lang('forum.settings.title')
@stop

@section('breadcrumbs')
  <li>{{ HTML::linkAction('forum.settings', Lang::get('forum.settings.title')) }}</li>
  <li>></li>
@stop

@section('body')
  <div class="pure-g">
    <div class="pure-u-1-5 settings-category">
      <div class="pure-menu pure-menu-open">
        <ul>
          <li class="pure-menu-heading">@lang('forum.settings.userinfo.title')</li>
          <li>{{ HTML::linkRoute('forum.settings.personal', Lang::get('forum.settings.personalinfo.title')) }}</li>
          <li>{{ HTML::linkRoute('forum.settings.account',  Lang::get('forum.settings.accountinfo.title')) }}</li>
          <li class="pure-menu-heading">@lang('forum.settings.admin.title')</li>
          <li>{{ HTML::linkRoute('forum.settings.admin.forums', Lang::get('forum.settings.admin.forums.title')) }}</li>
        </ul>
      </div>
    </div>
    
    <div class="pure-u-4-5 settings-body">
      @yield('settings')
    </div>
  </div>
@stop