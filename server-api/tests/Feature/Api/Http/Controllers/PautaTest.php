<?php

use App\Models\Pauta;
use Illuminate\Testing\Fluent\AssertableJson;

test('should create pauta', function () {
    $data = [
        'nome'      => 'Criando uma pauta teste',
        'descricao' => 'lorem ipsum',
    ];

    $response = $this->postJson('/api/pautas', $data);

    $response
        ->assertStatus(201)
        ->assertJson($data);
});

test('should be an error when create pauta', function () {
    $response = $this->post('/api/pautas');

    $response
        ->assertStatus(422)
        ->assertJson(fn(AssertableJson $json) =>
            $json
                ->has('message')
                ->has('errors'));
});

test('should update pauta', function () {
    $data = [
        'nome'      => 'Criando uma pauta teste',
        'descricao' => 'loreloresloores',
    ];

    $pauta = Pauta::create($data);

    $response = $this
        ->putJson('/api/pautas/' . $pauta->id, $data);

    $response
        ->assertOk()
        ->assertJson($data);
});

test('should delete pauta', function () {
    $data = [
        'nome'      => 'Criando uma pauta teste',
        'descricao' => 'loreloresloores',
    ];

    $pauta = Pauta::create($data);

    $response = $this
        ->delete('/api/pautas/' . $pauta->id);

    $response->assertStatus(204);
});
