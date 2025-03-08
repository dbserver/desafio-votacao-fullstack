<?php
namespace Tests\Feature;

function sum($n1, $n2)
{
    $calc = $n1 + $n2;
    return $calc;
}

test('sum', function () {
    $result = sum(1, 2);

    expect($result)->toBe(3);
});
