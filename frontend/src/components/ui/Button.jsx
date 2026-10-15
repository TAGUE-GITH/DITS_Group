import { Link } from "react-router-dom";

export default function Button({
  to,
  variant = "primary",
  block = false,
  className = "",
  children,
  ...props
}) {
  const classes = `btn btn-${variant} ${block ? "btn-block" : ""} ${className}`;

  if (to) {
    return (
      <Link to={to} className={classes} {...props}>
        {children}
      </Link>
    );
  }

  return (
    <button className={classes} {...props}>
      {children}
    </button>
  );
}