import { FaStar } from "react-icons/fa";
import "./Stars.css";

export default function Stars({ value = 0, size = 16 }) {
  return (
    <div className="stars" style={{ fontSize: size }}>
      {[1, 2, 3, 4, 5].map((n) => (
        <FaStar key={n} className={n <= value ? "star-filled" : "star-empty"} />
      ))}
    </div>
  );
}