<?php
use Illuminate\Foundation\Testing\DatabaseMigrations;

pest()
    ->use(DatabaseMigrations::class)
    ->extend(Tests\TestCase::class)
    ->in('Feature');
