<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Pauta extends Model
{
    use HasFactory;

    /**
     * Nome da tabela associada ao modelo.
     *
     * @var string
     */
    protected $table = 'pautas';

    /**
     * Atributos que podem ser preenchidos em massa (mass assignment).
     *
     * @var array
     */
    protected $fillable = [
        'nome',
        'descricao',
    ];

    /**
     * Atributos que devem ser ocultos para arrays ou JSON.
     *
     * @var array
     */
    protected $hidden = [
        // Adicione aqui campos sensíveis que não devem ser expostos
    ];

    /**
     * Atributos que devem ser convertidos para tipos nativos.
     *
     * @var array
     */
    protected $casts = [
        'created_at' => 'datetime',
        'updated_at' => 'datetime',
    ];

    /**
     * Relacionamentos do modelo (se houver).
     *
     * Exemplo:
     * public function user()
     * {
     *     return $this->belongsTo(User::class);
     * }
     */
}
