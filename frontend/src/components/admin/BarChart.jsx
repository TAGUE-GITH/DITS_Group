export default function BarChart({ items, stats }) {
  const values = items.map(([key]) => stats[key] || 0);
  const maxValue = Math.max(...values, 0);

  return (
    <div className="bar-chart">
      {items.map(([key, label, color]) => {
        const value = stats[key] || 0;
        const width = maxValue > 0 ? (value / maxValue) * 100 : 0;

        return (
          <div key={key} className="bar-row">
            <span className="bar-label">{label}</span>
            <div className="bar-track" aria-label={`${label}: ${value}`}>
              <span
                className="bar-fill"
                style={{ width: `${width}%`, background: color }}
              />
            </div>
            <span className="bar-value">{value}</span>
          </div>
        );
      })}
    </div>
  );
}
