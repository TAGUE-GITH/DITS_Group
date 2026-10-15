import { getArticles } from "../../api/articleApi";
import useFetch from "../../hooks/useFetch";
import SectionTitle from "../ui/SectionTitle";
import DataState from "../ui/DataState";
import Button from "../ui/Button";
import ArticleCard from "../cards/ArticleCard";

export default function ArticlesSection() {
  const { data, loading, error } = useFetch(getArticles);
  const articles = (data || []).slice(0, 3);

  // Pas encore d'article publie : la section n'apparait pas
  if (!loading && !error && !articles.length) return null;

  return (
    <section className="section">
      <div className="container">
        <SectionTitle
          tag="Actualités"
          title="Nos derniers articles"
          text="Conseils, retours d'expérience et nouveautés de DITS Group."
        />
        <DataState loading={loading} error={error} empty={!articles.length}>
          <div className="grid">
            {articles.map((article) => (
              <ArticleCard key={article.id} article={article} />
            ))}
          </div>
          <div className="section-action">
            <Button to="/articles" variant="dark">
              Tous les articles
            </Button>
          </div>
        </DataState>
      </div>
    </section>
  );
}