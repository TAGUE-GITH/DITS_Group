export default function AdminHeader({ title, text, children }) {
  return (
    <div className="admin-head">
      <div>
        <h1>{title}</h1>
        {text && <p>{text}</p>}
      </div>
      {children}
    </div>
  );
}