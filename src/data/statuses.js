export const userStatuses = {
  PENDING: { label: "En attente", tone: "warning" },
  ACTIVE: { label: "Actif", tone: "success" },
  DISABLED: { label: "Désactivé", tone: "danger" },
};

export const requestStatuses = {
  PENDING: { label: "En attente", tone: "warning" },
  IN_PROGRESS: { label: "En cours", tone: "info" },
  PROCESSED: { label: "Traitée", tone: "success" },
  REJECTED: { label: "Refusée", tone: "danger" },
};

export const applicationStatuses = {
  PENDING: { label: "En attente", tone: "warning" },
  IN_REVIEW: { label: "En étude", tone: "info" },
  ACCEPTED: { label: "Acceptée", tone: "success" },
  REJECTED: { label: "Refusée", tone: "danger" },
};

export const newsletterStatuses = {
  ACTIVE: { label: "Actif", tone: "success" },
  UNSUBSCRIBED: { label: "Désabonné", tone: "neutral" },
};

export const reviewStatuses = {
  PENDING: { label: "En attente de validation", tone: "warning" },
  APPROVED: { label: "Publié", tone: "success" },
  REJECTED: { label: "Non retenu", tone: "danger" },
};