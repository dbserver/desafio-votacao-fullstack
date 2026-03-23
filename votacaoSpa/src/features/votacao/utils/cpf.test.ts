import { describe, expect, it } from 'vitest'
import { formatCpf, normalizeCpf } from './cpf'

describe('cpf utils', () => {
  it('should format cpf progressively', () => {
    expect(formatCpf('1')).toBe('1')
    expect(formatCpf('123456')).toBe('123.456')
    expect(formatCpf('123456789')).toBe('123.456.789')
    expect(formatCpf('12345678901')).toBe('123.456.789-01')
  })

  it('should normalize cpf to digits only and max 11 chars', () => {
    expect(normalizeCpf('123.456.789-01')).toBe('12345678901')
    expect(normalizeCpf('123456789012345')).toBe('12345678901')
  })
})
