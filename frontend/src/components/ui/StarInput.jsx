import { useState } from "react";
import { FaStar } from "react-icons/fa";
import "./Stars.css";

export default function StarInput({ value, onChange }) {
  const [hover, setHover] = useState(0);

  return (
    <div className="star-input" onMouseLeave={() => setHover(0)}>
      {[1, 2, 3, 4, 5].map((n) => (
        <button
          type="button"
          key={n}
          className={n <= (hover || value) ? "star-filled" : "star-empty"}
          onMouseEnter={() => setHover(n)}
          onClick={() => onChange(n)}
          aria-label={`${n} étoile${n > 1 ? "s" : ""}`}
        >
          <FaStar />
        </button>
      ))}
    </div>
  );
}