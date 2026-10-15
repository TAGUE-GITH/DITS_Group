import "./Chips.css";

export default function Chips({ items, value, onChange, allLabel = "Tous" }) {
  if (!items.length) return null;

  return (
    <div className="filter-chips">
      {[{ value: "", label: allLabel }, ...items].map((item) => (
        <button
          key={item.value || "all"}
          className={`filter-chip ${value === item.value ? "filter-chip-active" : ""}`}
          onClick={() => onChange(item.value)}
        >
          {item.label}
        </button>
      ))}
    </div>
  );
}