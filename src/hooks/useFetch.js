import { useCallback, useEffect, useState } from "react";

export default function useFetch(fetcher) {
  const [state, setState] = useState({ data: null, loading: true, error: "" });
  const [version, setVersion] = useState(0);

  useEffect(() => {
    let active = true;

    // Le chargement demarre de facon asynchrone (pas de mise a jour d'etat synchrone dans l'effet)
    Promise.resolve()
      .then(() => active && setState((previous) => ({ ...previous, loading: true, error: "" })))
      .then(() => fetcher())
      .then((data) => active && setState({ data, loading: false, error: "" }))
      .catch(
        (err) =>
          active && setState({ data: null, loading: false, error: err.message })
      );

    return () => {
      active = false;
    };
  }, [fetcher, version]);

  const reload = useCallback(() => setVersion((current) => current + 1), []);

  return { ...state, reload };
}