export default function DonutChart({ items, stats, totalLabel }) {
  const total = items.reduce((sum, [key]) => sum + (stats[key] || 0), 0);
  let cumulative = 0;

  const stops = items
    .filter(([key]) => stats[key])
    .map(([key, , color]) => {
      const start = (cumulative / (total || 1)) * 360;
      cumulative += stats[key];
      const end = (cumulative / (total || 1)) * 360;
      return `${color} ${start}deg ${end}deg`;
    });

  const background = stops.length ? `conic-gradient(${stops.join(", ")})` : "var(--border)";

  return (
    <div className="donut-chart">
      <div className="donut" style={{ background }}>
        <div className="donut-hole">
          <strong>{total}</strong>
          <span>{totalLabel}</span>
        </div>
      </div>
      <ul className="stat-legend">
        {items.map(([key, label, color]) => (
          <li key={key}>
            <i style={{ background: color }} />
            {label} <strong>{stats[key] || 0}</strong>
          </li>
        ))}
      </ul>
    </div>
  );
}