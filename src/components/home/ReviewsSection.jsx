import { getReviews, getReviewsSummary } from "../../api/reviewApi";
import useFetch from "../../hooks/useFetch";
import SectionTitle from "../ui/SectionTitle";
import Stars from "../ui/Stars";
import "./ReviewsSection.css";

export default function ReviewsSection() {
  const { data: reviews } = useFetch(getReviews);
  const { data: summary } = useFetch(getReviewsSummary);

  if (!reviews?.length) return null;

  return (
    <section className="section">
      <div className="container">
        <SectionTitle
          tag="Avis clients"
          title="Ce que nos clients en pensent"
          text="Des retours authentiques de clients qui nous ont fait confiance."
        />
        {summary && (
          <div className="reviews-summary">
            <strong>{summary.averageRating.toFixed(1)}</strong>
            <div>
              <Stars value={Math.round(summary.averageRating)} />
              <span>{summary.totalReviews} avis</span>
            </div>
          </div>
        )}
        <div className="reviews-grid">
          {reviews.map((review) => (
            <div key={review.id} className="card review">
              <Stars value={review.rating} />
              <p>{review.comment}</p>
              <span className="review-author">{review.clientName}</span>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}