<?php

class DatabaseSeeder extends Seeder {
  public function run() {
    Eloquent::unguard();
    $this->call('TableTruncater');
    $this->call('TableSeeder');
    $this->call('TechSeeder');
  }
}

class TableSeeder extends Seeder {
  public function run() {
    Race::create([
      'name' => 'Swagmaster'
    ]);
    
    Stat::create([
      'name' => 'Strength',
      'desc' => 'Physical ability',
      'abbv' => 'STR'
    ]);
    
    Stat::create([
      'name' => 'Dexterity',
      'desc' => 'Finer skills',
      'abbv' => 'DEX'
    ]);
    
    Stat::create([
      'name' => 'Intelligence',
      'desc' => 'Mental ability',
      'abbv' => 'INT'
    ]);
    
    $user[] = User::create([
      'email'      => 'corey@narwhunderful.com',
      'password'   => Hash::make('monoxide'),
      'name_first' => 'Corey',
      'name_last'  => 'Frenette'
    ]);
    
    $user[] = User::create([
      'email'      => 'c-desmet@hotmail.com',
      'password'   => Hash::make('meowmix239'),
      'name_first' => 'Corina',
      'name_last'  => 'De Smet'
    ]);
    
    UserInfo::create([
      'user_id' => $user[0]->id
    ]);
    
    UserInfo::create([
      'user_id' => $user[1]->id
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user[0]->id,
      'question' => 'The answer to this question is 1',
      'answer' => '1'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user[0]->id,
      'question' => 'The answer to this question is 2',
      'answer' => '2'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user[0]->id,
      'question' => 'The answer to this question is 3',
      'answer' => '3'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user[1]->id,
      'question' => 'The answer to this question is 1',
      'answer' => '1'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user[1]->id,
      'question' => 'The answer to this question is 2',
      'answer' => '2'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user[1]->id,
      'question' => 'The answer to this question is 3',
      'answer' => '3'
    ]);
    
    $f[] = Forum::create([
      'name' => 'Malachite'
    ]);
    
    $f[] = Forum::create([
      'name' => 'Other'
    ]);
    
    $f[] = Forum::create([
      'parent_id' => $f[0]->id,
      'name'      => 'Announcements'
    ]);
    
    $f[] = Forum::create([
      'parent_id' => $f[1]->id,
      'name'      => 'Off-Topic'
    ]);
  }
}

class TechSeeder extends Seeder {
  public function run() {
    $building[] = Building::create([
      'name' => 'Camp',
      'desc' => 'A camp for stuff.',
      'type' => 'base'
    ]);
    
    $building[] = Building::create([
      'name' => 'Low-Income Housing',
      'desc' => '',
      'type' => 'housing'
    ]);
    
    $research[] = Research::create([
      'name' => 'Swag',
      'desc' => ''
    ]);
    
    /* Why the entire shit isn't this working?
    $req[] = Requirement::create([
      'unlock_id' => $building[1]->id,
      'unlock_type' => 'building',
      'requirement_id' => $research[0]->id,
      'requirement_type' => 'research'
    ]);*/
    
    $req[] = new Requirement;
    $req[0]->unlock_id = $building[1]->id;
    $req[0]->unlock_type = 'building';
    $req[0]->requirement_id = $research[0]->id;
    $req[0]->requirement_type = 'research';
    $req[0]->save();
  }
}

class TableTruncater extends Seeder {
  public function run() {
    $this->command->info('Getting foreign keys...');
    $t1 = microtime(true);
    
    // Get the database name
    $dbname = DB::connection('mysql')->getDatabaseName();
    
    // Find the FKs
    $fks = DB::table('INFORMATION_SCHEMA.KEY_COLUMN_USAGE')
            ->select('TABLE_NAME', 'COLUMN_NAME', 'CONSTRAINT_NAME', 'REFERENCED_TABLE_NAME', 'REFERENCED_COLUMN_NAME')
      ->whereNotNull('REFERENCED_TABLE_NAME')
               ->get();
    
    // Find the tables
    $tables = DB::table('INFORMATION_SCHEMA.TABLES')
               ->select('TABLE_SCHEMA', 'TABLE_NAME')
                ->where('TABLE_SCHEMA', '=', $dbname)
                ->where('TABLE_NAME', '<>', 'migrations')
                  ->get();
    
    $this->command->info('Killing foreign keys...');
    
    // Kill all FKs
    foreach($fks as $fk) {
      Schema::table($fk->TABLE_NAME, function($table) use($fk) {
        $table->dropForeign($fk->CONSTRAINT_NAME);
      });
    }
    
    $this->command->info('Truncating tables...');
    
    // Truncate all tables
    foreach($tables as $table) {
      DB::table($table->TABLE_NAME)->truncate();
    }
    
    $this->command->info('Reinstating foreign keys...');
    
    // Add all the FKs back
    foreach($fks as $fk) {
      Schema::table($fk->TABLE_NAME, function($table) use($fk) {
        $table->foreign($fk->COLUMN_NAME)
              ->references($fk->REFERENCED_COLUMN_NAME)
              ->on($fk->REFERENCED_TABLE_NAME);
      });
    }
    
    $this->command->info('Truncation completed in ' . (microtime(true) - $t1) . ' seconds.');
  }
}