<?php

namespace Database\Factories;

use App\Models\Sessao;
use App\Models\User;
use App\Models\Voto;
use Illuminate\Database\Eloquent\Factories\Factory;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Voto>
 */
class VotoFactory extends Factory
{
    protected $model = Voto::class;
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        $sessao = Sessao::factory()->create();

        return [
            'sessao_id' => $sessao->id,
            'user_id' => User::factory(),
            'voto' => true,
        ];
    }
}
