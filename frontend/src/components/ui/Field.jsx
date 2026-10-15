export default function Field({ label, id, as = "input", children, ...props }) {
  const Tag = as;

  return (
    <div className="field">
      {label && <label htmlFor={id}>{label}</label>}
      <Tag id={id} name={id} {...props}>
        {children}
      </Tag>
    </div>
  );
}