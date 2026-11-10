import { describe, expect, it } from 'vitest';
import { isDocument, isEmail, isHttpUrl, isInteger, isMoney, permits } from './validation';

describe('Validación de entradas y pegado', () => {
  it('rechaza exponentes, signos y letras en cantidades e importes', () => {
    for (const value of ['1e3', '-1', '+4', '2a', '1,50']) {
      expect(permits(value, 'digits')).toBe(false);
      expect(permits(value, 'money')).toBe(false);
    }
    expect(permits('12.34', 'money')).toBe(true);
    expect(permits('12.345', 'money')).toBe(false);
  });
  it('admite nombres con acentos y evita dígitos', () => {
    expect(permits('María O’Neill', 'name')).toBe(true);
    expect(permits('María123', 'name')).toBe(false);
  });
  it('valida límites numéricos de los contratos Java y PostgreSQL', () => {
    expect(isInteger('0')).toBe(true);
    expect(isInteger('0', 1)).toBe(false);
    expect(isInteger('2147483648')).toBe(false);
    expect(isMoney('0.00')).toBe(true);
    expect(isMoney('9999999999.99')).toBe(true);
    expect(isMoney('10000000000')).toBe(false);
    expect(isMoney('')).toBe(false);
  });
  it('distingue tipos de documento', () => {
    expect(isDocument('12345678', 'DNI')).toBe(true);
    expect(isDocument('123456789', 'DNI')).toBe(false);
    expect(isDocument('20123456789', 'RUC')).toBe(true);
    expect(isDocument('AB123456', 'PASSPORT')).toBe(true);
    expect(isDocument('AB 1234', 'CE')).toBe(false);
  });
  it('admite correo con etiquetas y bloquea imágenes de protocolos ejecutables', () => {
    expect(isEmail('maria+tienda@correo.com')).toBe(true);
    expect(isEmail('maria@')).toBe(false);
    expect(isHttpUrl('https://example.com/product.png')).toBe(true);
    expect(isHttpUrl('javascript:alert(1)')).toBe(false);
  });
});
