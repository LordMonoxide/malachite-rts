<?php

class Requirement extends Eloquent {
  public function unlock() {
    return $this->morphTo();
  }
  
  public function requirement() {
    return $this->morphTo();
  }
}