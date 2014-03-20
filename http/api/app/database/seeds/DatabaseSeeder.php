<?php

class DatabaseSeeder extends Seeder {
  public function run() {
    Eloquent::unguard();
    $this->call('TableTruncater');
    $this->call('UserTableSeeder');
  }
}

class UserTableSeeder extends Seeder {
  public function run() {
    $user = User::create([
      'email'      => 'corey@narwhunderful.com',
      'password'   => Hash::make('monoxide'),
      'name_first' => 'Corey',
      'name_last'  => 'Frenette'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user->id,
      'question' => 'The answer to this question is 1',
      'answer' => '1'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user->id,
      'question' => 'The answer to this question is 2',
      'answer' => '2'
    ]);
    
    UserSecurityQuestion::create([
      'user_id' => $user->id,
      'question' => 'The answer to this question is 3',
      'answer' => '3'
    ]);
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