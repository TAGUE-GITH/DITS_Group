import { splitTags } from "../../utils/format";
import "./Tags.css";

export default function Tags({ value, limit }) {
  const tags = splitTags(value).slice(0, limit);

  if (!tags.length) return null;

  return (
    <div className="tags">
      {tags.map((tag) => (
        <span key={tag}>{tag}</span>
      ))}
    </div>
  );
}