export default function StatusBadge({ map, value }) {
  const status = map[value] || { label: value, tone: "neutral" };

  return <span className={`status status-${status.tone}`}>{status.label}</span>;
}