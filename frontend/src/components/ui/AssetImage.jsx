import { useState } from "react";
import { toAssetUrl } from "../../api/http";

/** Image envoyee par l'administration ; si elle est introuvable, le visuel de remplacement s'affiche. */
export default function AssetImage({ src, alt, fallback, ...props }) {
  const [broken, setBroken] = useState(false);

  if (!src || broken) return fallback;

  return <img src={toAssetUrl(src)} alt={alt} loading="lazy" onError={() => setBroken(true)} {...props} />;
}
