export default function StatBar({ items, stats }) {
  const total = items.reduce((sum, [key]) => sum + (stats[key] || 0), 0);

  return (
    <div>
      <div className="stat-bar">
        {items.map(([key, label, color]) =>
          stats[key] ? (
            <span
              key={key}
              title={`${label} : ${stats[key]}`}
              style={{ width: `${(stats[key] / total) * 100}%`, background: color }}
            />
          ) : null
        )}
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