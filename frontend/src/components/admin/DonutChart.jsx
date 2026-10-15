export default function DonutChart({ items, stats, totalLabel }) {
  const total = items.reduce((sum, [key]) => sum + (stats[key] || 0), 0);

  // Angle de debut et de fin de chaque part, calcules sans variable modifiee pendant l'affichage
  const shown = items.filter(([key]) => stats[key]);
  const stops = shown.map(([key, , color], index) => {
    const before = shown.slice(0, index).reduce((sum, [previous]) => sum + stats[previous], 0);
    const start = (before / (total || 1)) * 360;
    const end = ((before + stats[key]) / (total || 1)) * 360;
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