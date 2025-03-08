<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Voto extends Model
{
    use HasFactory;

    /**
     * Os atributos que podem ser preenchidos em massa.
     *
     * @var array
     */
    protected $fillable = [
        'sessao_id',
        'user_id',
        'voto',
    ];

    protected $casts = [
        'voto' => 'boolean',
    ];

    /**
     * Relacionamento com a tabela `users`.
     */
    public function user()
    {
        return $this->belongsTo(User::class);
    }

    /**
     * Relacionamento com a tabela de sessões.
     */
    public function sessao()
    {
        return $this->belongsTo(Sessao::class);
    }
}
