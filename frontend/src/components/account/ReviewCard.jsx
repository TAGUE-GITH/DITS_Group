import { useState } from "react";
import { createReview, getMyReview } from "../../api/reviewApi";
import { reviewStatuses } from "../../data/statuses";
import useFetch from "../../hooks/useFetch";
import useSubmit from "../../hooks/useSubmit";
import StatusBadge from "../admin/StatusBadge";
import Stars from "../ui/Stars";
import StarInput from "../ui/StarInput";
import Field from "../ui/Field";
import Button from "../ui/Button";
import Alert from "../ui/Alert";

export default function ReviewCard() {
  const { data: review, loading, reload } = useFetch(getMyReview);
  const [rating, setRating] = useState(0);
  const [comment, setComment] = useState("");

  const { submit, loading: sending, error } = useSubmit(async () => {
    if (!rating) throw new Error("Choisissez une note de 1 à 5 étoiles.");

    await createReview({ rating, comment });
    reload();
  });

  if (loading) return null;

  return (
    <div className="card review-card">
      <h3>Mon avis</h3>

      {review ? (
        <>
          <Stars value={review.rating} />
          <p>{review.comment}</p>
          <StatusBadge map={reviewStatuses} value={review.status} />
        </>
      ) : (
        <form onSubmit={submit}>
          <Alert>{error}</Alert>
          <StarInput value={rating} onChange={setRating} />
          <Field
            label="Votre commentaire"
            id="comment"
            as="textarea"
            value={comment}
            onChange={(event) => setComment(event.target.value)}
            placeholder="Partagez votre expérience avec DITS Group..."
            required
          />
          <Button type="submit" disabled={sending} block>
            {sending ? "Envoi..." : "Envoyer mon avis"}
          </Button>
        </form>
      )}
    </div>
  );
}