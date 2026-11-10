/** Etiqueta y cantidad que muestra el indicador. */
export interface MetricCardProps {
  label: string;
  value: number;
}
/** Tarjeta de indicador administrativo de lectura rápida. */
export function MetricCard({ label, value }: MetricCardProps) {
  return (
    <article className="card metric-card">
      <p>{label}</p>
      <strong>{value}</strong>
      <small>Datos actuales de tu tienda</small>
    </article>
  );
}
