/** Reglas de edición: se aplican también al pegado, conservando la accesibilidad. */
export type CharacterRule =
  'text' | 'name' | 'digits' | 'money' | 'slug' | 'email' | 'phone' | 'document' | 'token';
/** Rechaza la edición completa si introduce caracteres no admitidos. */
export function permits(value: string, rule: CharacterRule = 'text'): boolean {
  const rules: Record<CharacterRule, RegExp> = {
    text: /^[^\u0000-\u001f\u007f]*$/u,
    name: /^[\p{L}\p{M} '\u2019.-]*$/u,
    digits: /^\d*$/,
    money: /^\d{0,10}(\.\d{0,2})?$/,
    slug: /^[a-z0-9-]*$/,
    email: /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~@-]*$/,
    phone: /^\+?\d{0,15}$/,
    document: /^[a-zA-Z0-9]*$/,
    token: /^[a-zA-Z0-9_-]*$/,
  };
  return rules[rule].test(value);
}
/** Valida correo completo sin prohibir direcciones con etiquetas +. */
export const isEmail = (value: string): boolean => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
/** Valida enteros no negativos en el rango de Integer del backend. */
export const isInteger = (value: string, min = 0): boolean =>
  /^\d+$/.test(value) && Number(value) >= min && Number(value) <= 2147483647;
/** Valida importes con dos decimales y hasta diez cifras enteras. */
export const isMoney = (value: string): boolean => /^\d{1,10}(\.\d{1,2})?$/.test(value);
/** Valida imágenes remotas y evita protocolos ejecutables. */
export function isHttpUrl(value: string): boolean {
  try {
    return ['http:', 'https:'].includes(new URL(value).protocol);
  } catch {
    return false;
  }
}
/** Tipos de documento requeridos por historias de registro. */
export type DocumentType = 'DNI' | 'RUC' | 'CE' | 'PASSPORT';
/** Valida longitud y caracteres sin inventar una verificación tributaria. */
export function isDocument(value: string, kind: string): boolean {
  if (kind === 'DNI') return /^\d{8}$/.test(value);
  if (kind === 'RUC') return /^\d{11}$/.test(value);
  return /^[a-zA-Z0-9]{6,20}$/.test(value);
}
