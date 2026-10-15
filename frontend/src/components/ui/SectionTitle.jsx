export default function SectionTitle({ tag, title, text }) {
  return (
    <div className="section-title">
      {tag && <span>{tag}</span>}
      <h2>{title}</h2>
      {text && <p>{text}</p>}
    </div>
  );
}