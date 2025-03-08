<?php

use App\Http\Controllers\ApiController;
use App\Http\Controllers\PautaController;
use App\Http\Controllers\ResultadoController;
use App\Http\Controllers\SessaoController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\VotoController;
use Illuminate\Support\Facades\Route;

Route::prefix('pautas')->group(function () {
    Route::get('/', [PautaController::class, 'index']);
    Route::post('/', [PautaController::class, 'store']);
    Route::get('/{id}', [PautaController::class, 'show']);
    Route::put('/{id}', [PautaController::class, 'update']);
    Route::delete('/{id}', [PautaController::class, 'destroy']);
});

Route::prefix('sessoes')->group(function () {
    Route::get('/', [SessaoController::class, 'index']);
    Route::post('/', [SessaoController::class, 'store']);
    Route::get('/{id}', [SessaoController::class, 'show']);
    Route::put('/{id}', [SessaoController::class, 'update']);
    Route::delete('/{id}', [SessaoController::class, 'destroy']);
});

Route::prefix('users')->group(function () {
    Route::get('/', [UserController::class, 'index']);
    Route::post('/', [UserController::class, 'store']);
    Route::get('/{id}', [UserController::class, 'show']);
    Route::put('/{id}', [UserController::class, 'update']);
    Route::delete('/{id}', [UserController::class, 'destroy']);
});

Route::prefix('votos')->group(function () {
    Route::get('/', [VotoController::class, 'index']);
    Route::post('/', [VotoController::class, 'store']);
    Route::get('/{id}', [VotoController::class, 'show']);
    Route::put('/{id}', [VotoController::class, 'update']);
    Route::delete('/{id}', [VotoController::class, 'destroy']);
});

Route::prefix('resultados')->group(function () {
    Route::get('/{pauta_id}', [ResultadoController::class, 'calcularResultados']);
});

Route::get('/', [ApiController::class, 'index']);
