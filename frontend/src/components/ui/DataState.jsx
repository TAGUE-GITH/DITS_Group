import Loader from "./Loader";
import Alert from "./Alert";

export default function DataState({
  loading,
  error,
  empty,
  emptyText = "Aucun élément disponible pour le moment.",
  children,
}) {
  if (loading) return <Loader />;
  if (error) return <Alert>{error}</Alert>;
  if (empty) return <p className="empty">{emptyText}</p>;
  return children;
}