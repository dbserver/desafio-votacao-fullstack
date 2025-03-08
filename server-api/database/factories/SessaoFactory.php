<?php

namespace Database\Factories;

use App\Models\Pauta;
use App\Models\Sessao;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Sessao>
 */
class SessaoFactory extends Factory
{
    protected $model = Sessao::class;

    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {

        return [
            'pauta_id' => Pauta::factory(),
            'data_inicio' => now()->format('Y-m-d H:i:00'),
            'data_final' => now()->addMinutes(1)->format('Y-m-d H:i:00'),
        ];
    }
}
