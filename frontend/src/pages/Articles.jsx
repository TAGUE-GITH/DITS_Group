import { useMemo, useState } from "react";
import { FiSearch } from "react-icons/fi";
import { getArticles } from "../api/articleApi";
import useFetch from "../hooks/useFetch";
import PageHeader from "../components/ui/PageHeader";
import DataState from "../components/ui/DataState";
import ArticleCard from "../components/cards/ArticleCard";
import usePageTitle from "../hooks/usePageTitle";
import "./Detail.css";

export default function Articles() {
  usePageTitle("Actualités et articles", "Conseils, retours d'expérience et nouveautés de DITS Group.");

  const [search, setSearch] = useState("");
  const { data, loading, error } = useFetch(getArticles);

  const articles = useMemo(() => {
    const term = search.trim().toLowerCase();

    return (data || []).filter((article) =>
      `${article.title} ${article.summary || ""} ${article.content || ""}`
        .toLowerCase()
        .includes(term)
    );
  }, [data, search]);

  return (
    <>
      <PageHeader
        title="Actualités et articles"
        text="Conseils, retours d'expérience et nouveautés de DITS Group."
      />
      <section className="section">
        <div className="container">
          <div className="search">
            <FiSearch />
            <input
              type="search"
              placeholder="Rechercher un article..."
              value={search}
              onChange={(event) => setSearch(event.target.value)}
            />
          </div>

          <DataState
            loading={loading}
            error={error}
            empty={!articles.length}
            emptyText="Aucun article ne correspond à votre recherche."
          >
            <div className="grid">
              {articles.map((article) => (
                <ArticleCard key={article.id} article={article} />
              ))}
            </div>
          </DataState>
        </div>
      </section>
    </>
  );
}