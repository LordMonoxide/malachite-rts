@extends('forum.layout')

@section('title')
@lang('forum.title')
@stop

@section('breadcrumbs')
          <li>{{ HTML::linkAction('forum.index', Lang::get('forum.index')) }}</li>
          <li>></li>
          <li>{{ HTML::linkAction('forum.category', $category->name, $category->path) }}</li>
          <li>></li>
@stop

@section('body')
      <table class="forum pure-table pure-table-horizontal pure-table-striped">
        <thead>
          <tr>
            <th>{{ HTML::linkAction('forum.category', $category->name, $category->path) }}</th>
          </tr>
        </thead>
        
        <tbody>
  @foreach($category->forums as $forum)
          <tr>
            <td>{{ HTML::linkAction('forum.view', $forum->name, [$category->path, $forum->path]) }}</td>
          </tr>
    @foreach($forum->children as $child)
          <tr>
            <td style="padding-left:2em">{{ HTML::linkAction('forum.view', $child->name, [$forum->category->path, $child->path]) }}</td>
          </tr>
    @endforeach
  @endforeach
        </tbody>
      </table>
@stop